package com.example.assu_fe_app.presentation.review.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ItemReviewBinding
import com.example.assu_fe_app.presentation.review.ReviewStoreActivity.ReviewItem

class ReviewItemAdapter(
    private val items: List<ReviewItem>
) : RecyclerView.Adapter<ReviewItemAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReviewItem) {
            binding.tvReviewItemStudent.text = item.student
            binding.tvReviewItemContent.text = item.content
            binding.tvReviewItemDate.text = item.date

            val starViews = listOf(
                binding.ivReviewStoreStar1,
                binding.ivReviewStoreStar2,
                binding.ivReviewStoreStar3,
                binding.ivReviewStoreStar4,
                binding.ivReviewStoreStar5
            )

            for (i in starViews.indices) {
                val drawableRes = if (i < item.rating) {
                    R.drawable.ic_location_item_star_selected
                } else {
                    R.drawable.ic_location_item_star_unselected
                }
                starViews[i].setBackgroundResource(drawableRes)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}