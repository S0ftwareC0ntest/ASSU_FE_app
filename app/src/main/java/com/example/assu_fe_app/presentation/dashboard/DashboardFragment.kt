package com.example.assu_fe_app.presentation.dashboard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.ServiceRecord
import com.example.assu_fe_app.ServiceRecordAdapter
import com.example.assu_fe_app.presentation.dashboard.ServiceSuggestActivity
import com.example.assu_fe_app.databinding.FragmentDashboardBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import java.time.LocalDateTime

class DashboardFragment : BaseFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {


    lateinit var serviceRecordAdapter: ServiceRecordAdapter

    override fun initObserver() {}

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView(){
        initClick()
        initAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initAdapter(){
        serviceRecordAdapter = ServiceRecordAdapter()
        binding.rvServiceRecord.apply {
            adapter = serviceRecordAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        serviceRecordAdapter.setData(createDummyData())

        binding.tvServiceCount.text = serviceRecordAdapter.itemCount.toString()
    }

    private fun initClick(){
        binding.btnSuggestService.setOnClickListener {
            val intent = Intent(requireContext(), ServiceSuggestActivity::class.java)
            startActivity(intent)
        }

        // 이전 달로 넘어가는 화살표
        binding.ivDashBackArrow.setOnClickListener {

        }


        // 다음 달로 넘어가는 화살표
        // 만약 현재 달과 다르다면 넘어가는 버튼을 활성화할 것
        // 현재 달이라면 비활성화 화살표를 띄울 것.
        binding.ivDashNextArrow.setOnClickListener {

        }
    }

    private fun updateUI(){
        if(serviceRecordAdapter.itemCount >=3 ){
            binding.tvDashSeeAll.visibility = View.VISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDummyData() : List<ServiceRecord>{
        return listOf(
            ServiceRecord(
                "숑숑 돈가스",
                "음료 한병을 제공받았어요",
                LocalDateTime.now(),
                false
            ),
            ServiceRecord(
                "밀플랜비",
                "소스를 제공받았어요",
                LocalDateTime.now(),
                false
            )
            ,
            ServiceRecord(
                "밀플랜비",
                "음료를 제공받았어요",
                LocalDateTime.now(),
                false
            )
        )
    }

}