package com.example.assu_fe_app.presentation.user.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.assu_fe_app.R
import com.example.assu_fe_app.data.dto.location.ViewportQuery
import com.example.assu_fe_app.databinding.FragmentUserLoactionBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.user.review.store.UserReviewStoreActivity
import com.example.assu_fe_app.ui.location.UserLocationViewModel
import com.kakao.vectormap.GestureType
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserLocationFragment :
    BaseFragment<FragmentUserLoactionBinding>(R.layout.fragment_user_loaction) {

    // Kakao Map
    private lateinit var mapView: MapView
    private lateinit var kakaoMap: KakaoMap

    // ViewModel
    private val vm: UserLocationViewModel by viewModels()

    // Location
    private val fused by lazy { LocationServices.getFusedLocationProviderClient(requireContext()) }
    private val permLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) fetchLocationAndQuery()
        else moveToDefaultThenQuery() // 퍼미션 거부 → 기본 위치로 조회
    }

    // 기본 위치(서울 시청 근처)
    private val DEFAULT_LATITUDE = 37.5662952
    private val DEFAULT_LONGITUDE = 126.9779451
    private val DEFAULT_ZOOM = 15               // << Int 로 변경!

    override fun initView() {
        binding.viewLocationSearchBar.setOnClickListener { navigateToSearch() }
        binding.ivLocationSearchIc.setOnClickListener { navigateToSearch() }
        binding.tvLocationHint.setOnClickListener { navigateToSearch() }

        binding.userLocationMapView.setOnClickListener {
            binding.includeSpeechBubble.visibility = View.VISIBLE
            binding.fvUserLocationItem.visibility = View.VISIBLE
        }

        binding.fvUserLocationItem.setOnClickListener {
            startActivity(Intent(requireContext(), UserReviewStoreActivity::class.java))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.userLocationMapView

        mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    Log.d("KakaoMap", "onMapDestroy (UserLocationFragment)")
                }
                override fun onMapError(error: Exception) {
                    Log.e("KakaoMap", "onMapError (UserLocationFragment)", error)
                }
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(map: KakaoMap) {
                    kakaoMap = map

                    // 카메라 이동 종료시 현재 화면 기준으로 재조회
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

                    // 최초 1회: 권한 확인 후 현재 위치로 이동 → 조회
                    checkPermissionAndRun()
                }
            }
        )

        // ViewModel state collect
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collect { state ->
                    when (state) {
                        is UserLocationViewModel.UiState.Idle -> Unit
                        is UserLocationViewModel.UiState.Loading -> {
                            Log.d("UIState", "Loading…")
                        }
                        is UserLocationViewModel.UiState.Success -> {
                            Log.d("UIState", "Loaded ${state.items.size} stores")
                            // TODO: 마커/리스트 갱신
                        }
                        is UserLocationViewModel.UiState.Fail -> {
                            Log.e("UIState", "Fail: ${state.code}, ${state.message}")
                        }
                        is UserLocationViewModel.UiState.Error -> {
                            Log.e("UIState", "Error", state.t)
                        }
                    }
                }
            }
        }
    }

    override fun initObserver() = Unit

    private fun navigateToSearch() {
        startActivity(Intent(requireContext(), UserLocationSearchActivity::class.java))
    }

    // ===== MapView lifecycle =====
    override fun onResume() {
        super.onResume()
        if (::mapView.isInitialized) mapView.resume()
    }
    override fun onPause() {
        super.onPause()
        if (::mapView.isInitialized) mapView.pause()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        if (::mapView.isInitialized) mapView.removeAllViews()
    }

    // ===== Permission & location =====
    private fun checkPermissionAndRun() {
        val fineGranted = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            fetchLocationAndQuery()
        } else {
            permLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @SuppressLint("MissingPermission") // checkPermissionAndRun() 에서 보장
    private fun fetchLocationAndQuery() {
        // 1) lastLocation 우선
        fused.lastLocation
            .addOnSuccessListener { loc ->
                if (loc != null) {
                    moveCameraAndQuery(loc.latitude, loc.longitude)
                } else {
                    // 2) 없으면 즉시 한 번 측위
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

        // 줌 레벨은 Int 사용!
        kakaoMap.moveCamera(
            CameraUpdateFactory.newCenterPosition(LatLng.from(lat, lng), DEFAULT_ZOOM)
        )

        // 초기 1회 바로 조회 (이후엔 moveEnd 리스너에서 자동)
        requestNearbyFromCurrentViewport()
    }

    private fun moveToDefaultThenQuery() {
        moveCameraAndQuery(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
    }

    private fun requestNearbyFromCurrentViewport() {
        if (!::kakaoMap.isInitialized || mapView.width == 0 || mapView.height == 0) return

        val vp = mapView.getViewportCorners(kakaoMap)
        val query = ViewportQuery(
            minLng = vp.minLng, minLat = vp.minLat,
            maxLng = vp.maxLng, maxLat = vp.maxLat
        )
        Log.d("Viewport", "query=$query")
        vm.load(query)
    }

    // ===== Viewport 계산 =====
    fun MapView.getViewportCorners(kakaoMap: KakaoMap): Viewport {
        val w = width   // Int
        val h = height  // Int

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

    data class Viewport(
        val minLng: Double, val minLat: Double,
        val maxLng: Double, val maxLat: Double,
        val nw: LatLng, val ne: LatLng, val se: LatLng, val sw: LatLng
    )
}