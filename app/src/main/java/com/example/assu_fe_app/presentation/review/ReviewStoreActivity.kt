package com.example.assu_fe_app.presentation.review

import android.content.Context
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivityReviewStoreBinding
import com.example.assu_fe_app.presentation.base.BaseActivity
import com.example.assu_fe_app.presentation.review.adapter.ReviewStoreAdapter

class ReviewStoreActivity :
    BaseActivity<ActivityReviewStoreBinding>(R.layout.activity_review_store) {

    data class ReviewStoreItem(
        val organization: String,
        val content: String
    )

    override fun initView() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val extraPaddingTop = 3
            v.setPadding(
                systemBars.left,
                systemBars.top + extraPaddingTop.dpToPx(v.context),
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        val reviewStoreList = listOf(
            ReviewStoreItem("총학생회", "4인 이상 식사시, 음료 제공"),
            ReviewStoreItem("IT대 학생회", "10% 할인"),
        )

        val adapter = ReviewStoreAdapter(reviewStoreList)
        binding.rcReviewStore.layoutManager = LinearLayoutManager(this)
        binding.rcReviewStore.adapter = adapter

    }

    override fun initObserver() {
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}