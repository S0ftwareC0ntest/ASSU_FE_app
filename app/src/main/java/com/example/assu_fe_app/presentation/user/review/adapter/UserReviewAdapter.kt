package com.example.assu_fe_app.presentation.user.review.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.data.dto.review.Review
import com.example.assu_fe_app.databinding.ItemReviewBinding

class UserReviewAdapter(
    private val showDeleteButton: Boolean = false
) : RecyclerView.Adapter<UserReviewViewHolder>() {

    private val reviewList = mutableListOf<Review>()

    fun setData(newList: List<Review>) {
        reviewList.clear()
        reviewList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserReviewViewHolder(binding, showDeleteButton)
    }

    override fun getItemCount() = reviewList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: UserReviewViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }
}