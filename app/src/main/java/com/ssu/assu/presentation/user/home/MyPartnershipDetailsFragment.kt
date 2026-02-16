package com.ssu.assu.presentation.user.home

import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssu.assu.R
import com.ssu.assu.databinding.FragmentMyPartnershipDetailsBinding
import com.ssu.assu.presentation.base.BaseFragment
import com.ssu.assu.presentation.user.dashboard.adapter.ServiceRecordAdapter
import com.ssu.assu.presentation.user.home.adapter.TemporaryQrDataAdapter
import com.ssu.assu.ui.usage.UnreviewedUsageViewModel
import com.ssu.assu.ui.user.UserHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPartnershipDetailsFragment :
    BaseFragment<FragmentMyPartnershipDetailsBinding>(R.layout.fragment_my_partnership_details) {

    private var serviceRecordAdapter: ServiceRecordAdapter? = null
    private var qrDataAdapter: TemporaryQrDataAdapter? = null

    private val viewModel : UnreviewedUsageViewModel by viewModels()
    private val stampViewModel: UserHomeViewModel by activityViewModels()

    private lateinit var stampViews: List<ImageView>

    override fun initObserver() {
        // 리뷰 목록 관찰 (ServiceRecord)
        viewModel.usageList.observe(viewLifecycleOwner) { records ->
            serviceRecordAdapter?.setData(records)
        }

        // 임시 QR 데이터 관찰 (스탬프 적립 내역)
        viewModel.qrDataList.observe(viewLifecycleOwner) { qrList ->
            qrDataAdapter?.submitList(qrList)
        }

        // 스탬프 UI 상태 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            stampViewModel.stampState.collect { state ->
                when (state) {
                    is UserHomeViewModel.StampUiState.Success -> {
                        updateStampDisplay(state.stampCount)
                    }
                    is UserHomeViewModel.StampUiState.Error -> {
                        updateStampDisplay(0)
                    }
                    else -> {}
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        binding.ivMyPartnershipBackArrow.setOnClickListener {
            navigateToHome()
        }

        setupRecyclerViewAdapter()
        initScrollListener()
        initializeStampViews()

        // 데이터 로드
        viewModel.getUnreviewedUsage()
        viewModel.getMyTemporaryData()
        stampViewModel.loadStampCount()
    }

    private fun initializeStampViews() {
        stampViews = listOf(
            binding.ivHomeStamp1, binding.ivHomeStamp2, binding.ivHomeStamp3,
            binding.ivHomeStamp4, binding.ivHomeStamp5, binding.ivHomeStamp6,
            binding.ivHomeStamp7, binding.ivHomeStamp8, binding.ivHomeStamp9,
            binding.ivHomeStamp10
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerViewAdapter() {
        binding.rvHomeMyPartnershipDetailsList.layoutManager = LinearLayoutManager(requireContext())

        // --- [현재 활성화] TemporaryQrDataAdapter 사용 ---
        qrDataAdapter = TemporaryQrDataAdapter()
        binding.rvHomeMyPartnershipDetailsList.adapter = qrDataAdapter


        // TODO : 기존에 리뷰되지 않은 제휴 사용내역 조회 시 이부분 다시 주석 해제 후 윗 부분 주석처리
        // serviceRecordAdapter = ServiceRecordAdapter()
        // binding.rvHomeMyPartnershipDetailsList.adapter = serviceRecordAdapter
    }

    private fun updateStampDisplay(stampCount: Int) {
        var realCount = stampCount % 10
        if (realCount == 0 && stampCount != 0) {
            realCount = 10
        }

        stampViews.forEachIndexed { index, imageView ->
            if (index < realCount) {
                imageView.setImageResource(R.drawable.ic_home_stamp_filled)
            } else {
                imageView.setImageResource(R.drawable.ic_home_stamp)
            }
        }
    }

    private fun initScrollListener() {
        binding.rvHomeMyPartnershipDetailsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // QR 데이터 모드일 때는 무한 스크롤 방지
                if (qrDataAdapter != null) return

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (lastVisibleItemPosition == totalItemCount - 1 && !viewModel.isFetchingReviews) {
                    viewModel.getUnreviewedUsage()
                }
            }
        })
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_myPartnershipFragment_to_homeFragment)
    }
}