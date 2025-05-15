package com.example.assu_fe_app.presentation.location.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.presentation.location.HomeMyPartnershipDetailsReviewItem
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.assu_fe_app.databinding.ItemMyPartnershipReviewListBinding


class HomeMyPartnershipDetailsReviewAdapter (
    private val items: List<HomeMyPartnershipDetailsReviewItem>
) : RecyclerView.Adapter<HomeMyPartnershipDetailsReviewAdapter.ViewHolder> () {

    inner class ViewHolder(private val binding: ItemMyPartnershipReviewListBinding)
        :RecyclerView.ViewHolder(binding.root) {

            fun bind(item: HomeMyPartnershipDetailsReviewItem, isLastItem: Boolean) {
                binding.tvPlaceName.text = item.placeName
                binding.tvDescription.text = item.description
                binding.tvDateTime.text = item.dateTime

                // 마지막 아이템이면 선 숨기기
                binding.viewLocationSearchResultItemLine.visibility =
                    if (isLastItem) View.GONE else View.VISIBLE
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyPartnershipReviewListBinding.inflate(
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