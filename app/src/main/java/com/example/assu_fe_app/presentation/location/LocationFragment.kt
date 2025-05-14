package com.example.assu_fe_app.presentation.location

import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentLoactionBinding
import com.example.assu_fe_app.presentation.base.BaseFragment

class LocationFragment :
BaseFragment<FragmentLoactionBinding>(R.layout.fragment_loaction) {

    override fun initView() {
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
        val intent = Intent(requireContext(), LocationSearchActivity::class.java)
        startActivity(intent)
    }
}