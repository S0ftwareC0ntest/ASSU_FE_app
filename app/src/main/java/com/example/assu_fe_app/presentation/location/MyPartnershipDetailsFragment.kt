package com.example.assu_fe_app.presentation.location

import androidx.navigation.fragment.findNavController
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentMyPartnershipDetailsBinding
import com.example.assu_fe_app.presentation.base.BaseFragment

class MyPartnershipDetailsFragment : BaseFragment<FragmentMyPartnershipDetailsBinding>(R.layout.fragment_my_partnership_details) {
    override fun initObserver() {
    }

    override fun initView() {
        binding.ivMyPartnershipBackArrow.setOnClickListener {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_myPartnershipFragment_to_homeFragment)
    }
}