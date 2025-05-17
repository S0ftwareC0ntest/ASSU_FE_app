package com.example.assu_fe_app.presentation.mypage.review

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.Review
import com.example.assu_fe_app.databinding.ItemReviewBinding

class ReviewAdapter : RecyclerView.Adapter<ReviewViewHolder>() {

    private val reviewList = mutableListOf<Review>()

    fun setData(newList: List<Review>) {
        reviewList.clear()
        reviewList.addAll(newList)

        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun getItemCount() = reviewList.size


    // 생성된 뷰홀더에 아이템을 바인드
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int){
        holder.bind(reviewList[position])
    }

}