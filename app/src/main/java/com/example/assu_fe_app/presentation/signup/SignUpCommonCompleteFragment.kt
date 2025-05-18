package com.example.assu_fe_app.presentation.signup

import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentSignUpCommonCompleteBinding
import com.example.assu_fe_app.presentation.base.BaseFragment

class SignUpCommonCompleteFragment : BaseFragment<FragmentSignUpCommonCompleteBinding>(R.layout.fragment_sign_up_common_complete){
    override fun initObserver() {
    }

    override fun initView() {
        val userName = "이호근"  // 이 부분을 실제 로그인된 사용자 이름으로 동적으로 대체 가능

        val welcomeText = getString(R.string.signup_welcome_format, userName)
        binding.tvSignupDoneUsername.text = welcomeText
    }
}