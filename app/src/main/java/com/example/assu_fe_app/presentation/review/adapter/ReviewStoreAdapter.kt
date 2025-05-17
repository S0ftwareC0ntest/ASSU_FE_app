package com.example.assu_fe_app.presentation.review.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.data.dto.review.ReviewStoreItem
import com.example.assu_fe_app.databinding.ItemReviewStoreBinding
import com.example.assu_fe_app.presentation.review.ReviewStoreActivity

class ReviewStoreAdapter(
    private val items: List<ReviewStoreItem>
) : RecyclerView.Adapter<ReviewStoreAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemReviewStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReviewStoreItem) {
            binding.tvReviewStoreItemOrganization.text = item.organization
            binding.tvReviewStoreItemContent.text = item.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewStoreBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}