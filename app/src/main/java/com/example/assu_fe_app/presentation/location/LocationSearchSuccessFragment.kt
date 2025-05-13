package com.example.assu_fe_app.presentation.location

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentLocationSearchRankBinding
import com.example.assu_fe_app.databinding.FragmentLocationSearchSuccessBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.location.adapter.LocationSearchRankAdapter
import com.example.assu_fe_app.presentation.location.adapter.LocationSearchSuccessAdapter

data class LocationSearchResultItem(
    val shopName: String,
    val organization: String,
    val content: String
)

class LocationSearchSuccessFragmentFragment :
    BaseFragment<FragmentLocationSearchSuccessBinding>(R.layout.fragment_location_search_success) {

    private lateinit var adapter: LocationSearchSuccessAdapter

    override fun initObserver() {}

    override fun initView() {
        val dummyList = listOf(
            LocationSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
            LocationSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
            LocationSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
            LocationSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
            LocationSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
            LocationSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
            LocationSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
            LocationSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
            LocationSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공")
        )
        adapter = LocationSearchSuccessAdapter(dummyList)
        binding.rvLocationSearchSuccess.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLocationSearchSuccess.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}