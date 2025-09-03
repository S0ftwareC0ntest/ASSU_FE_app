package com.example.assu_fe_app.presentation.partner.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentPartnerMypageBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.common.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PartnerMypageFragment
    : BaseFragment<FragmentPartnerMypageBinding>(R.layout.fragment_partner_mypage) {

    private val viewModel: PartnerMypageViewModel by viewModels()

    override fun initView() { /* no-op */ }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.logoutState.collectLatest { state ->
                when (state) {
                    is PartnerMypageViewModel.LogoutState.Done -> navigateToLoginAndClear()
                    else -> Unit
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClick()
    }

    private fun initClick() {
        // 알림 설정
        binding.clPartnerAccountComponent1.setOnClickListener {
            PartnerMypageAlarmDialogFragment()
                .show(childFragmentManager, "AlarmDialog")
        }

        // 로그아웃: unregister 완료되면 옵저버가 화면 전환
        binding.clPartnerAccountComponent2.setOnClickListener {
            viewModel.logoutAndUnregister()
        }
    }

    private fun navigateToLoginAndClear() {
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}