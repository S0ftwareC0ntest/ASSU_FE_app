package com.example.assu_fe_app.presentation.common.location.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.data.dto.location.LocationSearchResultItem
import com.example.assu_fe_app.databinding.ItemLocationSearchResultItemBinding
import com.example.assu_fe_app.presentation.user.review.store.UserReviewStoreActivity

class LocationSearchSuccessAdapter(
    private val items: List<LocationSearchResultItem>
) : RecyclerView.Adapter<LocationSearchSuccessAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemLocationSearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LocationSearchResultItem, isLastItem: Boolean) {
            binding.tvLocationSearchResultItemShopName.text = item.shopName
            binding.tvLocationSearchResultItemOrganization.text = item.organization
            binding.tvLocationItemContent.text = item.content

            // 마지막 아이템이면 구분선 숨기기
            binding.viewLocationSearchResultItemLine.visibility =
                if (isLastItem) View.GONE else View.VISIBLE

            // 아이템 클릭 시 ReviewStoreActivity로 이동
            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, UserReviewStoreActivity::class.java)
                // shopName 등 데이터 전달이 필요하다면 아래 추가
                // intent.putExtra("shopName", item.shopName)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLocationSearchResultItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isLast = position == items.size - 1
        holder.bind(items[position], isLast)
    }
}