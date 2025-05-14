package com.example.assu_fe_app.presentation.location.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ItemLocationSearchRankBinding
import com.example.assu_fe_app.databinding.ItemLocationSearchResultItemBinding
import com.example.assu_fe_app.presentation.location.LocationSearchItem
import com.example.assu_fe_app.presentation.location.LocationSearchResultItem

class LocationSearchSuccessAdapter(
    private val items: List<LocationSearchResultItem>
) : RecyclerView.Adapter<LocationSearchSuccessAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemLocationSearchResultItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LocationSearchResultItem, isLastItem: Boolean) {
            binding.tvLocationSearchResultItemShopName.text = item.shopName
            binding.tvLocationSearchResultItemOrganization.text = item.organization
            binding.tvLocationItemContent.text = item.content

            // 마지막 아이템이면 선 숨기기
            binding.viewLocationSearchResultItemLine.visibility =
                if (isLastItem) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLocationSearchResultItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isLast = position == items.size - 1
        holder.bind(items[position], isLast)
    }
}