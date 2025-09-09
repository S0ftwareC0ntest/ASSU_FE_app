package com.example.assu_fe_app.presentation.common.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentNotificationUnreadBinding
import com.example.assu_fe_app.domain.model.notification.NotificationModel
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationUnreadFragment : Fragment(R.layout.fragment_notification_unread) {

    private var _binding: FragmentNotificationUnreadBinding? = null
    private val binding get() = _binding!!
    private val vm: NotificationsViewModel by activityViewModels()
    private lateinit var adapter: NotificationAdapter
    private lateinit var role: NotificationActivity.Role

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotificationUnreadBinding.bind(view)

        role = (arguments?.getSerializable(ARG_ROLE) as? NotificationActivity.Role)
            ?: NotificationActivity.Role.PARTNER

        adapter = NotificationAdapter(onClick = ::handleClick)
        binding.rvNotificationUnread.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotificationUnread.adapter = adapter

        vm.refresh(status = "unread")

        viewLifecycleOwner.lifecycleScope.launch {
            vm.unreadState.collectLatest { st ->
                adapter.submitList(st.items)
                // 필요 시 로딩/에러 UI 처리
            }
        }

        // 무한 스크롤(원치 않으면 아래 블록 삭제)
        binding.rvNotificationUnread.addOnScrollListener(object : EndlessScrollListener() {
            override fun onLoadMore() = vm.loadMore("unread")
        })
    }

    private fun handleClick(item: NotificationModel) {
        // 필요 시 클릭 동작
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARG_ROLE = "arg_role"
        fun newInstance(role: NotificationActivity.Role) = NotificationUnreadFragment().apply {
            arguments = Bundle().apply { putSerializable(ARG_ROLE, role) }
        }
    }
}