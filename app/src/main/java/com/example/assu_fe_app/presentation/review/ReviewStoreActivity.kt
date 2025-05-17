package com.example.assu_fe_app.presentation.review

import android.content.Context
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivityReviewStoreBinding
import com.example.assu_fe_app.presentation.base.BaseActivity
import com.example.assu_fe_app.presentation.review.adapter.ReviewItemAdapter
import com.example.assu_fe_app.presentation.review.adapter.ReviewStoreAdapter

class ReviewStoreActivity :
    BaseActivity<ActivityReviewStoreBinding>(R.layout.activity_review_store) {

    data class ReviewStoreItem(
        val organization: String,
        val content: String
    )

    data class ReviewItem(
        val student: String,
        val rating: Int,
        val content: String,
        val date: String
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
            ReviewStoreItem("IT대 학생회", "10% 할인")
        )

        val adapter = ReviewStoreAdapter(reviewStoreList)
        binding.rcReviewStore.layoutManager = LinearLayoutManager(this)
        binding.rcReviewStore.adapter = adapter

        val reviewAdapter = ReviewItemAdapter(getDummyReviewData())
        binding.fcvReviewStoreRank.adapter = reviewAdapter
        binding.fcvReviewStoreRank.layoutManager = LinearLayoutManager(this)

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

    private fun getDummyReviewData(): List<ReviewItem> {
        return listOf(
            ReviewItem("IT대학 재학생", 3, "사장님이 너무 친절하세요! 제휴 이벤트 좋았어요!", "작성일 | 2025-03-15 18:36"),
            ReviewItem("경영대학 재학생", 5, "가성비 갑! 매장도 깔끔하고 분위기 좋아요.", "작성일 | 2025-03-14 12:20"),
            ReviewItem("사회과학대학 재학생", 4, "서비스는 좋았지만 양이 조금 적었어요.", "작성일 | 2025-03-12 09:45"),
            ReviewItem("자연과학대학 재학생", 2, "기대보단 아쉬웠어요. 맛은 무난했어요.", "작성일 | 2025-03-10 20:10")
        )
    }

}

