package com.example.assu_fe_app.presentation.location

import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentMyPartnershipDetailsBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.location.adapter.HomeMyPartnershipDetailsReviewAdapter

data class HomeMyPartnershipDetailsReviewItem(
    val placeName: String,
    val description: String,
    val dateTime: String,
)

class MyPartnershipDetailsFragment :
    BaseFragment<FragmentMyPartnershipDetailsBinding>(R.layout.fragment_my_partnership_details) {

    private lateinit var adapter: HomeMyPartnershipDetailsReviewAdapter

    override fun initObserver() {
    }

    override fun initView() {
        binding.ivMyPartnershipBackArrow.setOnClickListener {
            navigateToHome()
        }

        val dummyList = listOf(
            HomeMyPartnershipDetailsReviewItem("역전할머니맥주 숭실대점","역전할머니맥주에서 음료 한 병을 제공받았어요!","2025-03-15 18:57"),
            HomeMyPartnershipDetailsReviewItem("역전할머니맥주 숭실대점","역전할머니맥주에서 음료 한 병을 제공받았어요!","2025-03-15 18:57"),
            HomeMyPartnershipDetailsReviewItem("역전할머니맥주 숭실대점","역전할머니맥주에서 음료 한 병을 제공받았어요!","2025-03-15 18:57"),
            HomeMyPartnershipDetailsReviewItem("역전할머니맥주 숭실대점","역전할머니맥주에서 음료 한 병을 제공받았어요!","2025-03-15 18:57"),
            HomeMyPartnershipDetailsReviewItem("역전할머니맥주 숭실대점","역전할머니맥주에서 음료 한 병을 제공받았어요!","2025-03-15 18:57"),
            HomeMyPartnershipDetailsReviewItem("역전할머니맥주 숭실대점","역전할머니맥주에서 음료 한 병을 제공받았어요!","2025-03-15 18:57"),
            HomeMyPartnershipDetailsReviewItem("역전할머니맥주 숭실대점","역전할머니맥주에서 음료 한 병을 제공받았어요!","2025-03-15 18:57"),
            HomeMyPartnershipDetailsReviewItem("역전할머니맥주 숭실대점","역전할머니맥주에서 음료 한 병을 제공받았어요!","2025-03-15 18:57"),
            HomeMyPartnershipDetailsReviewItem("역전할머니맥주 숭실대점","역전할머니맥주에서 음료 한 병을 제공받았어요!","2025-03-15 18:57"),
            HomeMyPartnershipDetailsReviewItem("역전할머니맥주 숭실대점","역전할머니맥주에서 음료 한 병을 제공받았어요!","2025-03-15 18:57"),
            HomeMyPartnershipDetailsReviewItem("역전할머니맥주 숭실대점","역전할머니맥주에서 음료 한 병을 제공받았어요!","2025-03-15 18:57"),
            HomeMyPartnershipDetailsReviewItem("역전할머니맥주 숭실대점","역전할머니맥주에서 음료 한 병을 제공받았어요!","2025-03-15 18:57")
        )
        adapter = HomeMyPartnershipDetailsReviewAdapter(dummyList)
        binding.rvHomeMyPartnershipDetailsList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHomeMyPartnershipDetailsList.adapter = adapter
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_myPartnershipFragment_to_homeFragment)
    }
}