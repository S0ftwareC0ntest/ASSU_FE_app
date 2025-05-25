package com.example.assu_fe_app.presentation.admin.home

import androidx.navigation.Navigation
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentAdminHomeBinding
import com.example.assu_fe_app.presentation.base.BaseFragment

class AdminHomeFragment :
    BaseFragment<FragmentAdminHomeBinding>(R.layout.fragment_admin_home) {

    override fun initObserver() {
    }

    override fun initView() {
        binding.btnAdminHomeViewAll.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_admin_home_to_admin_view_partner_list)
        }
    }
}