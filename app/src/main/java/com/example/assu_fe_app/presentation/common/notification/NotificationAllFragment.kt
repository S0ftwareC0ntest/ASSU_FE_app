package com.example.assu_fe_app.presentation.common.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentNotificationAllBinding
import com.example.assu_fe_app.domain.model.notification.NotificationModel
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationAllFragment : Fragment(R.layout.fragment_notification_all) {

    private var _binding: FragmentNotificationAllBinding? = null
    private val binding get() = _binding!!
    private val vm: NotificationsViewModel by activityViewModels()
    private lateinit var adapter: NotificationAdapter
    private lateinit var role: NotificationActivity.Role

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotificationAllBinding.bind(view)

        role = (arguments?.getSerializable(ARG_ROLE) as? NotificationActivity.Role)
            ?: NotificationActivity.Role.PARTNER

        adapter = NotificationAdapter(onClick = ::handleClick)
        binding.rvNotificationAll.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotificationAll.adapter = adapter

        // 최초 로드
        vm.refresh(status = "all")

        // 상태 구독 (스와이프 제거 → isRefreshing 같은 처리 없음)
        viewLifecycleOwner.lifecycleScope.launch {
            vm.allState.collectLatest { st ->
                android.util.Log.d("NOTI_UI", "collect allState: items=${st.items.size}, loading=${st.loading}")
                adapter.submitList(st.items)
                // 필요 시 로딩/에러 UI 처리
            }
        }

        // 무한 스크롤(원치 않으면 아래 블록 삭제)
        binding.rvNotificationAll.addOnScrollListener(object : EndlessScrollListener() {
            override fun onLoadMore() = vm.loadMore("all")
        })
    }

    private fun handleClick(item: NotificationModel) {
        vm.onItemClickAndReload(item.id, activeTab = "all")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARG_ROLE = "arg_role"
        fun newInstance(role: NotificationActivity.Role) = NotificationAllFragment().apply {
            arguments = Bundle().apply { putSerializable(ARG_ROLE, role) }
        }
    }
}