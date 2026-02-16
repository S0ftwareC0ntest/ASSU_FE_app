package com.ssu.assu.presentation.user.home.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssu.assu.data.dto.certification.response.TemporaryQrResponseDto
import com.ssu.assu.databinding.ItemTemporaryDataListBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TemporaryQrDataAdapter : ListAdapter<TemporaryQrResponseDto, TemporaryQrDataAdapter.ViewHolder>(
    TemporaryQrDataDiffCallback
) {
    inner class ViewHolder(private val binding: ItemTemporaryDataListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: TemporaryQrResponseDto) {

            binding.tvDateTime.text = try {
                val ldt = LocalDateTime.parse(item.createdAt, DateTimeFormatter.ISO_DATE_TIME)
                ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            } catch (e: Exception) {
                item.createdAt?.split(".")?.get(0)?.replace("T", " ") ?: ""
            }

            if (item.sort == "SUGGEST") {
                binding.tvStampSort.text = "제휴 건의글 작성"
                val admin = item.adminName ?: "학생회"
                binding.tvDescription.text = "${admin}에 제휴를 건의해 스탬프가 적립되었어요!"
            } else {
                binding.tvStampSort.text = "A:SSU 앱 리뷰 작성"
                binding.tvDescription.text = "A:SSU 앱 리뷰를 작성해 스탬프가 적립되었어요!"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTemporaryDataListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

// DiffUtil을 Object로 선언하여 메모리 낭비 방지
object TemporaryQrDataDiffCallback : DiffUtil.ItemCallback<TemporaryQrResponseDto>() {
    override fun areItemsTheSame(
        oldItem: TemporaryQrResponseDto,
        newItem: TemporaryQrResponseDto
    ): Boolean {
        return oldItem.createdAt == newItem.createdAt && oldItem.adminName == newItem.adminName
    }

    override fun areContentsTheSame(
        oldItem: TemporaryQrResponseDto,
        newItem: TemporaryQrResponseDto
    ): Boolean {
        return oldItem == newItem
    }
}