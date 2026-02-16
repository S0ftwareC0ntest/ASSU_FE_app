package com.ssu.assu.presentation.user.home.temporary

import android.content.Intent
import androidx.activity.OnBackPressedCallback
import com.ssu.assu.databinding.FragmentUserTemporaryCompleteBinding
import com.ssu.assu.presentation.base.BaseFragment
import com.ssu.assu.R
import com.ssu.assu.presentation.user.UserMainActivity

class UserTemporaryCompleteFragment : BaseFragment<FragmentUserTemporaryCompleteBinding>(R.layout.fragment_user_temporary_complete) {
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