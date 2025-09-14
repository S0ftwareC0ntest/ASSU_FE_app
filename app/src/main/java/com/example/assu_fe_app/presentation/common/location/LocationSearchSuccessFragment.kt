package com.example.assu_fe_app.presentation.common.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.data.dto.location.LocationAdminPartnerSearchResultItem
import com.example.assu_fe_app.databinding.FragmentLocationSearchSuccessBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.common.location.adapter.AdminPartnerLocationAdapter
import com.example.assu_fe_app.presentation.common.location.adapter.LocationSharedViewModel

class LocationSearchSuccessFragment :
    BaseFragment<FragmentLocationSearchSuccessBinding>(R.layout.fragment_location_search_success) {

    private val sharedViewModel: LocationSharedViewModel by viewModels()

    private lateinit var adapter: AdminPartnerLocationAdapter

    override fun initObserver() {}

    override fun initView() {

        val dummyList = listOf(
            LocationAdminPartnerSearchResultItem(
                shopName = "역전할머니맥주 숭실대점1",
                address = "서울 동작구 사당로 36-1 서정캐슬",
                isPartnered = true,
                term = "2025.02.24 ~ 2025.06.15",
                id = "abc",
                paperId = 10L
            ),
            LocationAdminPartnerSearchResultItem(
                shopName = "역전할머니맥주 숭실대점2",
                address = "서울 동작구 사당로 36-1 서정캐슬",
                isPartnered = false,
                term = "",          // 제휴 아님 → 캡슐 숨기고 주소만 표시
                id = "abc",
                paperId = 11L
            )

        )
        sharedViewModel.locationList.value = dummyList
        adapter = AdminPartnerLocationAdapter(dummyList)
        binding.rvLocationSearchSuccess.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLocationSearchSuccess.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}