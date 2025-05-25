package com.example.assu_fe_app.presentation.user.review.writing

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivityStarReviewBinding
import com.example.assu_fe_app.presentation.base.BaseActivity

class UserStarReviewActivity : BaseActivity<ActivityStarReviewBinding>(R.layout.activity_star_review) {
    override fun initView() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val extraPaddingTop = 3
            v.setPadding(
                systemBars.left,
                systemBars.top + extraPaddingTop.dpToPx(v.context),
                systemBars.right,
                0
            )
            insets
        }

        // 별 정의
        val stars = listOf(
            binding.ivStarReviewStar1,
            binding.ivStarReviewStar2,
            binding.ivStarReviewStar3,
            binding.ivStarReviewStar4,
            binding.ivStarReviewStar5
        )

        fun setStars(rating: Int) {
            for (i in stars.indices) {
                val drawableRes = if (i < rating) R.drawable.ic_activated_star else R.drawable.ic_deactivated_star
                stars[i].setImageResource(drawableRes)
            }
        }

        val deactivatedButton = binding.layoutWriteReviewDeactivatedButton
        val activatedButton = binding.layoutWriteReviewActivatedButton

        for ((index, star) in stars.withIndex()) {
            star.setOnClickListener {
                setStars(index + 1)
                deactivatedButton.visibility= View.GONE
                activatedButton.visibility=View.VISIBLE
            }
        }

        // 뒤로 가기 버튼 (activity -> fragment 전환. 그저 백스텝이어서 finish로 activity 끝냄)
        val backButton = binding.ivStarReviewBackArrow
        backButton.setOnClickListener {
            finish() // 액티비티 종료 → 이전 화면(프래그먼트 혹은 액티비티)로 돌아감
        }

        val writeReviewButton = binding.layoutWriteReviewActivatedButton
        writeReviewButton.setOnClickListener {
            val intent = Intent(this, UserPhotoReviewActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initObserver() {
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}