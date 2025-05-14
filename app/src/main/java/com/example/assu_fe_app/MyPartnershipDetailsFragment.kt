package com.example.assu_fe_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
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