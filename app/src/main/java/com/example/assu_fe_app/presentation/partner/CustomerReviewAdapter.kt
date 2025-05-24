package com.example.assu_fe_app.presentation.partner.review.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.data.dto.review.Review
import com.example.assu_fe_app.databinding.ItemReviewBinding
import com.example.assu_fe_app.presentation.user.review.adapter.ReviewStoreViewHolder

class CustomerReviewAdapter() : RecyclerView.Adapter<ReviewStoreViewHolder>() {

    private val reviewList = mutableListOf<Review>()

    fun setData(newList: List<Review>) {
        reviewList.clear()
        reviewList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewStoreViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewStoreViewHolder(binding)
    }

    override fun getItemCount() = reviewList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ReviewStoreViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }
}