package com.example.assu_fe_app.presentation.common.location

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.data.dto.location.LocationSearchResultItem
import com.example.assu_fe_app.databinding.FragmentLocationSearchSuccessBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.common.location.adapter.LocationSearchSuccessAdapter

class LocationSearchSuccessFragment :
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