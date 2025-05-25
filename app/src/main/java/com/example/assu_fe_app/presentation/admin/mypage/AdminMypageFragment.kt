package com.example.assu_fe_app.presentation.admin.mypage


import android.content.Intent
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentAdminMypageBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.common.login.LoginActivity
import kotlin.jvm.java


class AdminMypageFragment : BaseFragment<FragmentAdminMypageBinding>(R.layout.fragment_admin_mypage) {
    override fun initView(){
        // 알림 설정 탭
        binding.clAdmAccountComponent1.setOnClickListener {

        }

        // 로그아웃 탭
        binding.clAdmAccountComponent2.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            // 기존의 mainActivity를 삭제함
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun initObserver() {


    }


}

