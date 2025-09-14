package com.example.assu_fe_app.presentation.common.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
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
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.common.chatting.ChattingActivity
import com.example.assu_fe_app.presentation.common.location.adapter.AdminPartnerLocationAdapter
import com.example.assu_fe_app.presentation.common.location.adapter.LocationSharedViewModel
import com.example.assu_fe_app.ui.chatting.ChattingViewModel
import com.example.assu_fe_app.ui.location.AdminPartnerLocationViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
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


    // 채팅
    private val chatVm: ChattingViewModel by viewModels()

    // 위치 + 목록 조회 (Admin/Partner 공용)
    private val vm: AdminPartnerLocationViewModel by viewModels()

    private val role: UserRole by lazy {
        tokenManager.getUserRoleEnum() ?: UserRole.ADMIN // 기본값: ADMIN
    }

    // 위치 권한
    private val fused by lazy { LocationServices.getFusedLocationProviderClient(requireContext()) }
    private val permLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { r ->
        val granted = r[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                r[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) fetchLocationAndQuery() else moveToDefaultThenQuery()
    }

    // 기본 카메라
    private val DEFAULT_LATITUDE = 37.5662952
    private val DEFAULT_LONGITUDE = 126.9779451
    private val DEFAULT_ZOOM = 15

    override fun initView() {
        // 임시 리스트 (기존 로직 유지)
        val dummyList = listOf(
            LocationAdminPartnerSearchResultItem("역전할머니맥주 숭실대점1", "서울 동작구 사당로 36-1 서정캐슬", true, "2025.02.24 ~ 2025.06.15"),
            LocationAdminPartnerSearchResultItem("역전할머니맥주 숭실대점2", "서울 동작구 사당로 36-1 서정캐슬", false, "")
        )
        sharedViewModel.locationList.value = dummyList
        adapter = AdminPartnerLocationAdapter(dummyList)

        binding.viewLocationSearchBar.setOnClickListener { navigateToSearch() }
        binding.ivLocationSearchIc.setOnClickListener { navigateToSearch() }
        binding.tvLocationHint.setOnClickListener { navigateToSearch() }

        binding.viewLocationMap.setOnClickListener {
            binding.fvLocationItem.visibility = View.VISIBLE
        }

        binding.fvLocationItem.setOnClickListener {
            val item = currentItem ?: return@setOnClickListener

            // TODO 실제 id 연결
            val storeId = 1L
            val partnerId = 5L

            val entryMessage = if (item.isPartnered) {
                "'제휴 계약서 보기' 버튼을 통해 이동했습니다."
            } else {
                "'문의하기' 버튼을 통해 이동했습니다.이거야?"
            }

            chatVm.createRoom(CreateChatRoomRequestDto(adminId = storeId, partnerId = partnerId))
            binding.root.tag = entryMessage
        }
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

                    // 카메라 이동 종료 시 재조회
                    kakaoMap.setOnCameraMoveEndListener(
                        object : KakaoMap.OnCameraMoveEndListener {
                            override fun onCameraMoveEnd(
                                map: KakaoMap,
                                cameraPosition: CameraPosition,
                                gestureType: GestureType
                            ) {
                                requestNearbyFromCurrentViewport()
                            }
                        }
                    )

                    // 진입 시 현재 위치 기준으로 이동+조회
                    checkPermissionAndRun()
                }
            }
        )

        // 채팅 상태 수집 (기존 유지)
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

        // 목록 상태 수집 (Admin/Partner 조회 결과)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collect { s ->
                    when (s) {
                        is AdminPartnerLocationViewModel.UiState.Idle -> Unit
                        is AdminPartnerLocationViewModel.UiState.Loading -> Log.d("UIState", "Loading…")
                        is AdminPartnerLocationViewModel.UiState.PartnerSuccess -> {
                            Log.d("UIState", "Partners: ${s.items.size}")
                            // TODO: 마커/리스트 갱신
                        }
                        is AdminPartnerLocationViewModel.UiState.AdminSuccess -> {
                            Log.d("UIState", "Admins: ${s.items.size}")
                            // TODO: 마커/리스트 갱신
                        }
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

    // ===== 현재 위치 & 조회 =====
    private fun checkPermissionAndRun() {
        val fine = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (fine || coarse) fetchLocationAndQuery()
        else permLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocationAndQuery() {
        fused.lastLocation
            .addOnSuccessListener { loc ->
                if (loc != null) moveCameraAndQuery(loc.latitude, loc.longitude)
                else {
                    val cts = CancellationTokenSource()
                    fused.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
                        .addOnSuccessListener { cur ->
                            if (cur != null) moveCameraAndQuery(cur.latitude, cur.longitude)
                            else moveToDefaultThenQuery()
                        }
                        .addOnFailureListener {
                            Log.e("Location", "getCurrentLocation failed", it)
                            moveToDefaultThenQuery()
                        }
                }
            }
            .addOnFailureListener {
                Log.e("Location", "lastLocation failed", it)
                moveToDefaultThenQuery()
            }
    }

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
        val q = ViewportQuery(vp.minLng, vp.minLat, vp.maxLng, vp.maxLat)
        vm.load(role, q)  // 역할에 따라 다른 유즈케이스 호출
    }

    // ===== Viewport 계산 =====
    private fun MapView.getViewportCorners(kakaoMap: KakaoMap): Viewport {
        val w = width
        val h = height

        val nw = kakaoMap.fromScreenPoint(0, 0)!!
        val ne = kakaoMap.fromScreenPoint(w, 0)!!
        val se = kakaoMap.fromScreenPoint(w, h)!!
        val sw = kakaoMap.fromScreenPoint(0, h)!!

        val lngs = listOf(nw.longitude, ne.longitude, se.longitude, sw.longitude)
        val lats = listOf(nw.latitude, ne.latitude, se.latitude, sw.latitude)

        val minLng = lngs.minOrNull() ?: 0.0
        val maxLng = lngs.maxOrNull() ?: 0.0
        val minLat = lats.minOrNull() ?: 0.0
        val maxLat = lats.maxOrNull() ?: 0.0

        return Viewport(minLng, minLat, maxLng, maxLat, nw, ne, se, sw)
    }

    private data class Viewport(
        val minLng: Double, val minLat: Double,
        val maxLng: Double, val maxLat: Double,
        val nw: LatLng, val ne: LatLng, val se: LatLng, val sw: LatLng
    )

    // ===== lifecycle =====
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
}