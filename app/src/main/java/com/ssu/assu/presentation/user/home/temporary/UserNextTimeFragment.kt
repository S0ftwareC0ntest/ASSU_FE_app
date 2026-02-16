package com.ssu.assu.presentation.user.home.temporary

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.ssu.assu.R
import com.ssu.assu.databinding.FragmentUserNextTimeBinding
import com.ssu.assu.presentation.base.BaseFragment
import com.ssu.assu.presentation.user.UserMainActivity
import com.ssu.assu.presentation.user.home.UserHomeFragment


class UserNextTimeFragment : BaseFragment<FragmentUserNextTimeBinding>(R.layout.fragment_user_next_time) {
    override fun initObserver() {

    }


    override fun initView() {
        binding.clNextMyStamp.setOnClickListener {
            val intent = Intent(requireActivity(), UserMainActivity::class.java).apply {
                putExtra("nav_dest_id", R.id.myPartnershipDetailsFragment)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

            // UserMainActivity 시작
            startActivity(intent)
            activity?.finish()
        }

        binding.ivNextCross.setOnClickListener {
            // Activity 종료 후 HomeFragment가 있는 이전 화면으로 돌아감
            requireActivity().finish()
        }
        val callback: Any = object : OnBackPressedCallback(true) { // true로 콜백을 활성화합니다.
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            callback as OnBackPressedCallback
        )


    }

}