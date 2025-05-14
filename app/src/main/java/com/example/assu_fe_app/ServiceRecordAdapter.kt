package com.example.assu_fe_app

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.databinding.ItemServiceRecordBinding

class ServiceRecordAdapter : RecyclerView.Adapter<ServiceRecordViewHolder>() {
    private val serviceRecordList = mutableListOf<ServiceRecord>()

    fun setData(newList: List<ServiceRecord>){
        serviceRecordList.clear()
        serviceRecordList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceRecordViewHolder {
        val binding = ItemServiceRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceRecordViewHolder(binding)
    }

    override fun getItemCount() = serviceRecordList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ServiceRecordViewHolder, position: Int) {
        holder.bind(serviceRecordList[position])
    }
}