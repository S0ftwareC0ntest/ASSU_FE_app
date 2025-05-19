package com.example.assu_fe_app.presentation.user.home

import androidx.navigation.fragment.findNavController
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentHomeBinding
import com.example.assu_fe_app.presentation.base.BaseFragment

class HomeFragment :
    BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){
    override fun initObserver() {

    }

    override fun initView() {
        binding.tvSeeMoreMyStamp.setOnClickListener {
            navigateToMyPartnershipDetails()
        }
        binding.ivSeeMoreMyStamp.setOnClickListener {
            navigateToMyPartnershipDetails()
        }
    }
    private fun navigateToMyPartnershipDetails() {
        findNavController().navigate(R.id.myPartnershipDetailsFragment)
    }

}
