package com.example.assu_fe_app.presentation.common.location

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.assu_fe_app.R
import com.example.assu_fe_app.data.dto.UserRole
import com.example.assu_fe_app.data.dto.chatting.request.CreateChatRoomRequestDto
import com.example.assu_fe_app.data.dto.location.LocationAdminPartnerSearchResultItem
import com.example.assu_fe_app.data.dto.location.ViewportQuery
import com.example.assu_fe_app.data.manager.TokenManager
import com.example.assu_fe_app.databinding.FragmentLoactionBinding
import com.example.assu_fe_app.domain.model.location.AdminOnMap
import com.example.assu_fe_app.domain.model.location.PartnerOnMap
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.common.chatting.ChattingActivity
import com.example.assu_fe_app.presentation.common.location.adapter.AdminPartnerLocationAdapter
import com.example.assu_fe_app.presentation.common.location.adapter.LocationSharedViewModel
import com.example.assu_fe_app.ui.chatting.ChattingViewModel
import com.example.assu_fe_app.ui.location.AdminPartnerLocationViewModel
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationFragment :
    BaseFragment<FragmentLoactionBinding>(R.layout.fragment_loaction) {

    private val sharedViewModel: LocationSharedViewModel by activityViewModels()
    private lateinit var adapter: AdminPartnerLocationAdapter
    private var currentItem: LocationAdminPartnerSearchResultItem? = null

    private lateinit var mapView: MapView
    private lateinit var kakaoMap: KakaoMap

    @Inject lateinit var tokenManager: TokenManager

    private var poiLayer: LabelLayer? = null
    private var partnerStyles: LabelStyles? = null
    private var adminStyles: LabelStyles? = null

    // 라벨 ↔ 데이터 매핑
    private val labelToPartner = mutableMapOf<Label, PartnerOnMap>()
    private val labelToAdmin   = mutableMapOf<Label, AdminOnMap>()

    // 채팅
    private val chatVm: ChattingViewModel by viewModels()

    // 위치 + 목록 조회 (Admin/Partner 공용)
    private val vm: AdminPartnerLocationViewModel by viewModels()

    private val role: UserRole by lazy {
        // tokenManager.getUserRoleEnum() ?: UserRole.ADMIN
        UserRole.PARTNER // 테스트 강제
    }

    // 위치 권한 (현재는 테스트용으로 주석 흐름 유지)
    private val fused by lazy { LocationServices.getFusedLocationProviderClient(requireContext()) }
    private val permLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { r ->
        val granted = r[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                r[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            // fetchLocationAndQuery()
            moveToDefaultThenQuery()
        } else {
            moveToDefaultThenQuery()
        }
    }

    // 기본 카메라(서울 시청 근처)
    private val DEFAULT_LATITUDE = 37.5662952
    private val DEFAULT_LONGITUDE = 126.9779451
    private val DEFAULT_ZOOM = 17

    override fun initView() {
        binding.viewLocationSearchBar.setOnClickListener { navigateToSearch() }
        binding.ivLocationSearchIc.setOnClickListener { navigateToSearch() }
        binding.tvLocationHint.setOnClickListener { navigateToSearch() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.viewLocationMap

        mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() { Log.d("KakaoMap", "onMapDestroy") }
                override fun onMapError(error: Exception) { Log.e("KakaoMap", "onMapError", error) }
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(map: KakaoMap) {
                    kakaoMap = map

                    // 라벨 클릭 리스너
                    kakaoMap.setOnLabelClickListener { _, _, label ->
                        handleLabelClick(label)
                        true
                    }

                    // 마커 스타일 준비
                    val partnerBmp = vectorToBitmap(R.drawable.ic_marker, 24)
                    partnerStyles = kakaoMap.labelManager?.addLabelStyles(
                        LabelStyles.from(LabelStyle.from(partnerBmp).setAnchorPoint(0.5f, 1.0f))
                    )
                    val adminBmp = vectorToBitmap(R.drawable.ic_marker, 24)
                    adminStyles = kakaoMap.labelManager?.addLabelStyles(
                        LabelStyles.from(LabelStyle.from(adminBmp).setAnchorPoint(0.5f, 1.0f))
                    )

                    poiLayer = kakaoMap.labelManager?.layer

                    kakaoMap.setOnCameraMoveEndListener { _, _, _ -> requestNearbyFromCurrentViewport() }

                    moveToDefaultThenQuery()
                }
            }
        )

        // 채팅 상태 수집
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                chatVm.createRoomState.collect { state ->
                    when (state) {
                        is ChattingViewModel.CreateRoomUiState.Idle -> setCreateLoading(false)
                        is ChattingViewModel.CreateRoomUiState.Loading -> setCreateLoading(true)
                        is ChattingViewModel.CreateRoomUiState.Success -> {
                            setCreateLoading(false)
                            val intent = Intent(requireContext(), ChattingActivity::class.java).apply {
                                putExtra("roomId", state.data.roomId)
                                (binding.root.tag as? String)?.let { putExtra("entryMessage", it) }
                            }
                            startActivity(intent)
                            chatVm.resetCreateState()
                        }
                        is ChattingViewModel.CreateRoomUiState.Fail -> {
                            setCreateLoading(false)
                            Toast.makeText(requireContext(),
                                "채팅방 생성 실패(${state.code}) ${state.message ?: ""}",
                                Toast.LENGTH_SHORT).show()
                        }
                        is ChattingViewModel.CreateRoomUiState.Error -> {
                            setCreateLoading(false)
                            Toast.makeText(requireContext(),
                                "오류: ${state.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // 목록 상태 수집 + 마커 표시
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collect { s ->
                    when (s) {
                        is AdminPartnerLocationViewModel.UiState.Idle -> Unit
                        is AdminPartnerLocationViewModel.UiState.Loading -> Log.d("UIState", "Loading…")
                        is AdminPartnerLocationViewModel.UiState.PartnerSuccess -> drawMarkersPartners(s.items)
                        is AdminPartnerLocationViewModel.UiState.AdminSuccess -> drawMarkersAdmins(s.items)
                        is AdminPartnerLocationViewModel.UiState.Fail ->
                            Log.e("UIState", "Fail: ${s.code}, ${s.message}")
                        is AdminPartnerLocationViewModel.UiState.Error ->
                            Log.e("UIState", "Error", s.t)
                    }
                }
            }
        }
    }

    override fun initObserver() {
        sharedViewModel.locationList.observe(viewLifecycleOwner) { list ->
            val item = list.getOrNull(1) ?: return@observe
            currentItem = item
            val fragment = childFragmentManager.findFragmentById(R.id.fv_location_item) as? LocationItemFragment
            fragment?.showCapsuleInfo(item)
        }
    }

    private fun navigateToSearch() {
        startActivity(Intent(requireContext(), LocationSearchActivity::class.java))
    }

    private fun setCreateLoading(loading: Boolean) {
        binding.fvLocationItem.isEnabled = !loading
    }

    // ===== 카메라 이동 & 조회 =====
    private fun moveCameraAndQuery(lat: Double, lng: Double) {
        if (!::kakaoMap.isInitialized) return
        kakaoMap.moveCamera(
            CameraUpdateFactory.newCenterPosition(LatLng.from(lat, lng), DEFAULT_ZOOM)
        )
        requestNearbyFromCurrentViewport()
    }

    private fun moveToDefaultThenQuery() {
        moveCameraAndQuery(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
    }

    private fun requestNearbyFromCurrentViewport() {
        if (!::kakaoMap.isInitialized || mapView.width == 0 || mapView.height == 0) return
        val vp = mapView.getViewportCorners(kakaoMap)
        val q = ViewportQuery(
            lng1 = vp.nw.longitude, lat1 = vp.nw.latitude,
            lng2 = vp.ne.longitude, lat2 = vp.ne.latitude,
            lng3 = vp.se.longitude, lat3 = vp.se.latitude,
            lng4 = vp.sw.longitude, lat4 = vp.sw.latitude
        )
        vm.load(role, q)
    }

    private fun MapView.getViewportCorners(kakaoMap: KakaoMap): Viewport {
        val w = width
        val h = height
        val nw = kakaoMap.fromScreenPoint(0, 0)!!
        val ne = kakaoMap.fromScreenPoint(w, 0)!!
        val se = kakaoMap.fromScreenPoint(w, h)!!
        val sw = kakaoMap.fromScreenPoint(0, h)!!

        return Viewport(
            minLng = listOf(nw.longitude, ne.longitude, se.longitude, sw.longitude).minOrNull() ?: 0.0,
            minLat = listOf(nw.latitude, ne.latitude, se.latitude, sw.latitude).minOrNull() ?: 0.0,
            maxLng = listOf(nw.longitude, ne.longitude, se.longitude, sw.longitude).maxOrNull() ?: 0.0,
            maxLat = listOf(nw.latitude, ne.latitude, se.latitude, sw.latitude).maxOrNull() ?: 0.0,
            nw = nw, ne = ne, se = se, sw = sw
        )
    }

    private data class Viewport(
        val minLng: Double, val minLat: Double,
        val maxLng: Double, val maxLat: Double,
        val nw: LatLng, val ne: LatLng, val se: LatLng, val sw: LatLng
    )

    // ===== 파트너 마커 찍기 (ADMIN에서 보는 목록) =====
    private fun drawMarkersPartners(items: List<PartnerOnMap>) {
        val layer = poiLayer ?: kakaoMap.labelManager?.layer ?: return
        val styles = partnerStyles ?: return

        labelToPartner.clear()
        labelToAdmin.clear()
        layer.removeAll()

        items.forEach { p ->
            val label = layer.addLabel(
                LabelOptions.from(LatLng.from(p.latitude, p.longitude))
                    .setStyles(styles)
            )
            labelToPartner[label] = p
        }
    }

    // ===== 관리자(기관) 마커 찍기 (PARTNER에서 보는 목록) =====
    private fun drawMarkersAdmins(items: List<AdminOnMap>) {
        val layer = poiLayer ?: kakaoMap.labelManager?.layer ?: return
        val styles = adminStyles ?: return

        labelToPartner.clear()
        labelToAdmin.clear()
        layer.removeAll()

        items.forEach { a ->
            val label = layer.addLabel(
                LabelOptions.from(LatLng.from(a.latitude, a.longitude))
                    .setStyles(styles)
            )
            labelToAdmin[label] = a
        }
    }

    // ===== 라벨 클릭 처리 =====
    private fun handleLabelClick(label: Label) {
        // Partner 마커
        labelToPartner[label]?.let { p ->
            val item = LocationAdminPartnerSearchResultItem(
                shopName    = p.name,
                address     = p.address ?: "",
                isPartnered = p.partnered,
                term        = if (p.partnershipStartDate != null && p.partnershipEndDate != null)
                    "${p.partnershipStartDate} ~ ${p.partnershipEndDate}" else "",
                id          = p.partnerId.toString(),
                paperId     = p.partnerId
            )
            showCapsule(item)
            return
        }

        // Admin 마커
        labelToAdmin[label]?.let { a ->
            val item = LocationAdminPartnerSearchResultItem(
                shopName    = a.name,
                address     = a.address ?: "",
                isPartnered = a.partnered,
                term        = if (a.partnershipStartDate != null && a.partnershipEndDate != null)
                    "${a.partnershipStartDate} ~ ${a.partnershipEndDate}" else "",
                id          = a.adminId.toString(),
                paperId     = a.adminId
            )
            showCapsule(item)
            return
        }
    }

    // ===== 캡슐 프래그먼트 표시 =====
    private fun showCapsule(item: LocationAdminPartnerSearchResultItem) {
        val frag = childFragmentManager.findFragmentById(R.id.fv_location_item) as? LocationItemFragment
            ?: LocationItemFragment().also {
                childFragmentManager.beginTransaction()
                    .replace(R.id.fv_location_item, it)
                    .commitNowAllowingStateLoss()
            }

        frag.showCapsuleInfo(item)
        binding.fvLocationItem.apply {
            visibility = View.VISIBLE
            bringToFront()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::mapView.isInitialized) mapView.removeAllViews()
    }
    override fun onResume() {
        super.onResume()
        if (::mapView.isInitialized) mapView.resume()
    }
    override fun onPause() {
        super.onPause()
        if (::mapView.isInitialized) mapView.pause()
    }

    // ===== 벡터 → 비트맵 =====
    private fun vectorToBitmap(resId: Int, targetDp: Int): Bitmap {
        val d = ContextCompat.getDrawable(requireContext(), resId)
            ?: throw IllegalArgumentException("Drawable not found")
        val density = resources.displayMetrics.density
        val w = (targetDp * density).toInt().coerceAtLeast(1)
        val h = (targetDp * density).toInt().coerceAtLeast(1)
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        d.setBounds(0, 0, canvas.width, canvas.height)
        d.draw(canvas)
        return bmp
    }
}