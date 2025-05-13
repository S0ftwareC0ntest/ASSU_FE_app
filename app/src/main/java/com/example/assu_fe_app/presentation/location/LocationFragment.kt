package com.example.assu_fe_app.presentation.location

import androidx.navigation.fragment.findNavController
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentLoactionBinding
import com.example.assu_fe_app.presentation.base.BaseFragment

class LocationFragment :
BaseFragment<FragmentLoactionBinding>(R.layout.fragment_loaction) {

    override fun initView() {
        // 클릭 시 검색 프래그먼트로 이동
        binding.viewLocationSearchBar.setOnClickListener {
            navigateToSearch()
        }
        binding.ivLocationSearchIc.setOnClickListener {
            navigateToSearch()
        }
        binding.tvLocationHint.setOnClickListener {
            navigateToSearch()
        }
    }

    override fun initObserver() {
    }

    private fun navigateToSearch() {
        findNavController().navigate(R.id.action_locationFragment_to_locationSearchFragment)
    }
}