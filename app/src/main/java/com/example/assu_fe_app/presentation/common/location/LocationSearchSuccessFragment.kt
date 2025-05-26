package com.example.assu_fe_app.presentation.common.location

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.data.dto.location.LocationAdminPartnerSearchResultItem
import com.example.assu_fe_app.data.dto.location.LocationUserSearchResultItem
import com.example.assu_fe_app.databinding.FragmentLocationSearchSuccessBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.common.location.adapter.AdminPartnerLocationAdapter
import com.example.assu_fe_app.presentation.user.location.adapter.UserLocationSearchSuccessAdapter

class LocationSearchSuccessFragment :
    BaseFragment<FragmentLocationSearchSuccessBinding>(R.layout.fragment_location_search_success) {

//    private lateinit var adapter: UserLocationSearchSuccessAdapter
//
//    override fun initObserver() {}
//
//    override fun initView() {
//        val dummyList = listOf(
//            LocationUserSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
//            LocationUserSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
//            LocationUserSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
//            LocationUserSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
//            LocationUserSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
//            LocationUserSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
//            LocationUserSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
//            LocationUserSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공"),
//            LocationUserSearchResultItem("역전할머니맥주 숭실대점", "IT대학", "4인이상 식사시, 음료제공")
//        )
//        adapter = UserLocationSearchSuccessAdapter(dummyList)
//        binding.rvLocationSearchSuccess.layoutManager = LinearLayoutManager(requireContext())
//        binding.rvLocationSearchSuccess.adapter = adapter
//    }

    private lateinit var adapter: AdminPartnerLocationAdapter

    override fun initObserver() {}

    override fun initView() {
        val dummyList = listOf(
            LocationAdminPartnerSearchResultItem("역전할머니맥주 숭실대점", "서울 동작구 사당로 36-1 서정캐슬", true, "2025.02.24 ~ 2025.06.15"),
            LocationAdminPartnerSearchResultItem("역전할머니맥주 숭실대점", "서울 동작구 사당로 36-1 서정캐슬", false, "")
        )
        adapter = AdminPartnerLocationAdapter(dummyList)
        binding.rvLocationSearchSuccess.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLocationSearchSuccess.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}