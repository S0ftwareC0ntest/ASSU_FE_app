package com.example.assu_fe_app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.databinding.ItemServiceRecordBinding
import java.time.format.DateTimeFormatter

//class ServiceRecordViewHolder(private val binding: ItemServiceRecordBinding) : RecyclerView.ViewHolder(binding.root){
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun bind(serviceRecord: ServiceRecord){
//        binding.tvMarket.text = serviceRecord.marketName
//        binding.tvServiceExplain.text = serviceRecord.serviceContent
//
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
//        binding.tvServiceDatetime.text = serviceRecord.dateTime.format(formatter)
//
////        if(!serviceRecord.isReviewd)
//    }
//}