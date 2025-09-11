package com.example.assu_fe_app.presentation.admin.mypage


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentAdminMypageBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.common.login.LoginActivity
import com.example.assu_fe_app.presentation.common.login.LoginViewModel
import com.example.assu_fe_app.data.manager.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AdminMypageFragment : BaseFragment<FragmentAdminMypageBinding>(R.layout.fragment_admin_mypage) {
    
    @Inject
    lateinit var tokenManager: TokenManager
    
    private val loginViewModel: LoginViewModel by viewModels()
    
    override fun initView(){
        // UI 초기화만 수행
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
    }
    
    private fun initClickListeners() {
        // 알림 설정 탭
        binding.clAdmAccountComponent1.setOnClickListener {
            val alarmDialogFragment = AdminMypageAlarmDialogFragment()
            alarmDialogFragment.show(childFragmentManager, "AlarmDialog")
        }

        // 대기중인 제휴계약서 탭
        binding.clAdmAccountComponent3.setOnClickListener {
            val pendingDialogFragment = AdminMypagePendingPartnershipDialogFragment()
            pendingDialogFragment.show(childFragmentManager, "PendingPartnershipDialog")
        }

        // 로그아웃 탭
        binding.clAdmAccountComponent2.setOnClickListener {
            // 서버에 로그아웃 API 호출 후 토큰 삭제 및 로그인 화면으로 이동
            loginViewModel.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            // 기존의 mainActivity를 삭제함
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun initObserver() {


    }


}

