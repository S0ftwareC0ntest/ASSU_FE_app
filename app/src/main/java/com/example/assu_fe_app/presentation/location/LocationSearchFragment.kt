package com.example.assu_fe_app.presentation.location

import androidx.navigation.fragment.findNavController
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentLoactionBinding
import com.example.assu_fe_app.databinding.FragmentLoactionSearchBinding
import com.example.assu_fe_app.presentation.base.BaseFragment

class LocationSearchFragment  :
    BaseFragment<FragmentLoactionSearchBinding>(R.layout.fragment_loaction_search) {

    override fun initView() {
        binding.fragmentLocationSearchLeftArrow.setOnClickListener {
            findNavController().navigate(R.id.action_locationSearchFragment_to_locationFragment)
        }
    }

    override fun initObserver() {
    }
}