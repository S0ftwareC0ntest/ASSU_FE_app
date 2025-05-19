package com.example.assu_fe_app.presentation.common.signup

import android.content.Intent
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentSignUpCommonCompleteBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.MainActivity

class SignUpCommonCompleteFragment : BaseFragment<FragmentSignUpCommonCompleteBinding>(R.layout.fragment_sign_up_common_complete){
    override fun initObserver() {
    }

    override fun initView() {
        val userName = "이호근"  // 이 부분을 실제 로그인된 사용자 이름으로 동적으로 대체 가능

        val welcomeText = getString(R.string.signup_welcome_format, userName)
        binding.tvSignupDoneUsername.text = welcomeText

        // 회원가입 완료 후 Main Activity로 이동
        binding.btnCompleted.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}