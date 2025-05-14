package com.example.assu_fe_app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.MainActivity
import com.example.assu_fe_app.databinding.FragmentDashboardBinding
import java.time.LocalDateTime

class DashboardFragment : Fragment() {

    lateinit var binding: FragmentDashboardBinding
    lateinit var serviceRecordAdapter: ServiceRecordAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= FragmentDashboardBinding.inflate(layoutInflater)

        initClick()
        initAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
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