package com.example.assu_fe_app.presentation.mypage.review

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.R
import com.example.assu_fe_app.data.dto.review.Review
import com.example.assu_fe_app.databinding.ItemReviewBinding
import java.time.format.DateTimeFormatter

class ReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root){
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(review: Review){
        binding.tvMarket.text = review.marketName
        binding.tvReviewContent.text = review.content

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        binding.tvReviewDate.text = "작성일 | ${review.date.format(formatter)}"

        setRating(review.rate)

        val imageViews = listOf(
            binding.ivReviewImg1,
            binding.ivReviewImg2,
            binding.ivReviewImg3
        )

        // 초기에는 보이지 않게끔
        imageViews.forEach { it.visibility= View.GONE }


        // 이후 gradle에 Glide 라이브러리 추가 후 구현
        review.reviewImage.take(3).forEachIndexed{ index, imageUrls ->
            imageViews[index].visibility = View.VISIBLE

        }

    }

    private fun setRating(rating : Int){
        val starViews = listOf(
            binding.reviewStar1,
            binding.reviewStar2,
            binding.reviewStar3,
            binding.reviewStar4,
            binding.reviewStar5
        )

        for (i in starViews.indices){
            val colorRes = if(i <rating ) R.color.assu_main
                            else R.color.assu_font_sub
            starViews[i].setColorFilter(
                ContextCompat.getColor(itemView.context, colorRes)
            )
        }
    }
}