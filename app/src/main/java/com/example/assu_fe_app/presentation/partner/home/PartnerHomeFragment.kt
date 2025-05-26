package com.example.assu_fe_app.presentation.partner.home

import androidx.navigation.Navigation
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentPartnerHomeBinding
import com.example.assu_fe_app.presentation.base.BaseFragment

class PartnerHomeFragment :
    BaseFragment<FragmentPartnerHomeBinding>(R.layout.fragment_partner_home) {

    override fun initObserver() {
    }

    override fun initView() {
        binding.btnPartnerHomeViewAll.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_partner_home_to_partner_view_admin_list)
        }
    }
}