package com.example.assu_fe_app

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.databinding.ItemServiceRecordBinding
import java.time.format.DateTimeFormatter

//class ServiceRecordAdapter : RecyclerView.Adapter<ServiceRecordViewHolder>() {
//    private val serviceRecordList = mutableListOf<ServiceRecord>()
//
//    fun setData(newList: List<ServiceRecord>){
//        serviceRecordList.clear()
//        serviceRecordList.addAll(newList)
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceRecordViewHolder {
//        val binding = ItemServiceRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ServiceRecordViewHolder(binding)
//    }
//
//    override fun getItemCount() = serviceRecordList.size
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onBindViewHolder(holder: ServiceRecordViewHolder, position: Int) {
//        holder.bind(serviceRecordList[position])
//    }
//}

class ServiceRecordAdapter : RecyclerView.Adapter<ServiceRecordAdapter.ServiceRecordViewHolder>() {

    private val serviceRecordList = mutableListOf<ServiceRecord>()

    fun setData(newList: List<ServiceRecord>) {
        serviceRecordList.clear()
        serviceRecordList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount() = serviceRecordList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceRecordViewHolder {
        val binding = ItemServiceRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceRecordViewHolder(binding)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ServiceRecordViewHolder, position: Int) {
        holder.bind(serviceRecordList[position])
    }

    // 내부 ViewHolder 클래스
    inner class ServiceRecordViewHolder(private val binding: ItemServiceRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(serviceRecord: ServiceRecord) {
            binding.tvMarket.text = serviceRecord.marketName
            binding.tvServiceExplain.text = serviceRecord.serviceContent

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            binding.tvServiceDatetime.text = serviceRecord.dateTime.format(formatter)

        }
    }
}
