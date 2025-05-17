package com.example.assu_fe_app.presentation.review

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.data.dto.review.Review
import com.example.assu_fe_app.databinding.ActivityReviewStoreBinding
import com.example.assu_fe_app.presentation.base.BaseActivity
import com.example.assu_fe_app.presentation.review.adapter.ReviewAdapter
import com.example.assu_fe_app.presentation.review.adapter.ReviewStoreAdapter
import java.time.LocalDateTime

class ReviewStoreActivity :
    BaseActivity<ActivityReviewStoreBinding>(R.layout.activity_review_store) {

    private lateinit var reviewAdapter: ReviewAdapter

    data class ReviewStoreItem(
        val organization: String,
        val content: String
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        // 시스템 바 여백 적용
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

        // 제휴 혜택 리스트
        val reviewStoreList = listOf(
            ReviewStoreItem("총학생회", "4인 이상 식사시, 음료 제공"),
            ReviewStoreItem("IT대 학생회", "10% 할인"),
            ReviewStoreItem("IT대 학생회", "10% 할인")
        )
        val adapter = ReviewStoreAdapter(reviewStoreList)
        binding.rcReviewStore.layoutManager = LinearLayoutManager(this)
        binding.rcReviewStore.adapter = adapter

        // 리뷰 어댑터 초기화 및 바인딩
        reviewAdapter = ReviewAdapter(showDeleteButton = false)
        reviewAdapter.setData(createDummyData())
        binding.fcvReviewStoreRank.layoutManager = LinearLayoutManager(this)
        binding.fcvReviewStoreRank.adapter = reviewAdapter

        // 전체보기 클릭 시 상세 Fragment로 전환
        binding.tvReviewStoreReviewAll.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.review_store_fragment_container, ReviewStoreDetailFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun initObserver() {}

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    // 임시 더미 리뷰 데이터 생성
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDummyData(): List<Review> {
        return listOf(
            Review(
                marketName = "스시천국",
                rate = 5,
                content = "진짜 맛있었어요! 또 가고 싶어요!",
                date = LocalDateTime.now().minusDays(1),
                reviewImage = listOf()
            ),
            Review(
                marketName = "돈까스집",
                rate = 4,
                content = "튀김이 바삭해서 좋았어요. 양도 많아요.",
                date = LocalDateTime.now().minusDays(3),
                reviewImage = listOf()
            )
        )
    }
}