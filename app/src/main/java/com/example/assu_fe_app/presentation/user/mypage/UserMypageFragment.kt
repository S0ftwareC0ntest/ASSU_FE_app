package com.example.assu_fe_app.presentation.user.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.assu_fe_app.R
import com.example.assu_fe_app.data.manager.TokenManager
import com.example.assu_fe_app.databinding.FragmentUserMypageBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.common.login.LoginActivity
import com.example.assu_fe_app.presentation.common.login.LoginViewModel
import com.example.assu_fe_app.presentation.user.review.mypage.UserMyReviewActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserMypageFragment : BaseFragment<FragmentUserMypageBinding>(R.layout.fragment_user_mypage) {

    @Inject
    lateinit var tokenManager: TokenManager
    
    private val loginViewModel: LoginViewModel by viewModels()

    override fun initView(){

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClick() // 여기서 호출
    }

    private fun initClick(){
        binding.clAccountComponent1.setOnClickListener {
            val intent = Intent(requireContext(), UserMyReviewActivity::class.java)
            startActivity(intent)
        }

        // 프로필 수정 페이지
        binding.clAccountComponent2.setOnClickListener {

        }

        // 개인정보 처리방침 안내
        binding.clAccountComponent4.setOnClickListener {
            val privacyDialogFragment = UserMypagePrivacyDialogFragment()
            privacyDialogFragment.show(childFragmentManager, "PrivacyDialog")
        }

        // 자주 묻는 질문
        binding.clAccountComponent5.setOnClickListener {
            val faqDialogFragment = UserMypageFAQDialogFragment()
            faqDialogFragment.show(childFragmentManager, "FAQDialog")
        }

        // 고객센터
        binding.clAccountComponent6.setOnClickListener {
            val customerServiceDialogFragment = UserCustomerServiceDialogFragment()
            customerServiceDialogFragment.show(childFragmentManager, "CustomerServiceDialog")
        }


        //로그아웃 페이지
        binding.clAccountComponent3.setOnClickListener {
            // 서버에 로그아웃 API 호출 후 토큰 삭제 및 로그인 화면으로 이동
            loginViewModel.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            // 기존에 있던 메인액팁티를 메모리에서 삭제함.
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


    }

    override fun initObserver() {}

}