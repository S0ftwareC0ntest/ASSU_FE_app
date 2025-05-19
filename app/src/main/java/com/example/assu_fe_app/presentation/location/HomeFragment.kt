package com.example.assu_fe_app.presentation.location

import androidx.navigation.fragment.findNavController
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentHomeBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import android.content.Intent

class HomeFragment :
    BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){
    override fun initObserver() {

    }

    override fun initView() {
        // 제휴 QR 박스 클릭 시 인증 액티비티로 이동
        binding.clHomeQrBox.setOnClickListener {
            val intent = Intent(requireContext(), UserQRVerifyActivity::class.java)
            startActivity(intent)
        }

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
