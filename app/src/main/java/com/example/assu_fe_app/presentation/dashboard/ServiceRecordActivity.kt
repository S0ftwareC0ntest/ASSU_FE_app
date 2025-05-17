package com.example.assu_fe_app.presentation.dashboard

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.data.dto.servicerecord.ServiceRecord
import com.example.assu_fe_app.presentation.dashboard.adapter.ServiceRecordAdapter
import com.example.assu_fe_app.databinding.ActivityServiceRecordBinding
import com.example.assu_fe_app.presentation.base.BaseActivity
import java.time.LocalDateTime

class ServiceRecordActivity : BaseActivity<ActivityServiceRecordBinding>(R.layout.activity_service_record) {

    lateinit var serviceRecordAdapter: ServiceRecordAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView(){
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val extraPaddingTop = 3
            v.setPadding(
                systemBars.left,
                systemBars.top + extraPaddingTop.dpToPx(v.context),
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        binding.btnServiceRecordBack.setOnClickListener {
            finish()
        }

        initAdapter()
    }

    override fun initObserver(){

    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initAdapter(){
        serviceRecordAdapter = ServiceRecordAdapter()
        binding.rvServiceRecord.apply {
            adapter = serviceRecordAdapter
            layoutManager = LinearLayoutManager(this@ServiceRecordActivity)
        }
        serviceRecordAdapter.setData(createDummyData())

        binding.tvServiceCount.text = serviceRecordAdapter.itemCount.toString()
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
            ,
            ServiceRecord(
                "밀플랜비",
                "음료를 제공받았어요",
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
            ,
            ServiceRecord(
                "밀플랜비",
                "음료를 제공받았어요",
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