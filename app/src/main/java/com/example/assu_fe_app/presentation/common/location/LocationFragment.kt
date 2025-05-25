package com.example.assu_fe_app.presentation.common.location

import android.content.Intent
import android.view.View
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentLoactionBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.user.review.store.UserReviewStoreActivity

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

        binding.viewLocationMap.setOnClickListener{
            binding.fvLocationItem.visibility = View.VISIBLE
        }

        binding.fvLocationItem.setOnClickListener {
            val intent = Intent(requireContext(), UserReviewStoreActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initObserver() {
    }

    private fun navigateToSearch() {
        val intent = Intent(requireContext(), LocationSearchActivity::class.java)
        startActivity(intent)
    }
}