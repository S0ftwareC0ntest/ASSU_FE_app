package com.example.assu_fe_app.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.assu_fe_app.presentation.review.MyReviewActivity
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentMypageBinding
import com.example.assu_fe_app.presentation.base.BaseFragment

class MypageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    override fun initView(){

    }

    override fun initObserver() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClick() // 여기서 호출
    }

    private fun initClick(){
        binding.clAccountComponent1.setOnClickListener {
            val intent = Intent(requireContext(), MyReviewActivity::class.java)
            startActivity(intent)
        }
    }


}