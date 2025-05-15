package com.example.assu_fe_app.presentation.location

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isInvisible
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivityStarReviewBinding
import com.example.assu_fe_app.presentation.base.BaseActivity

class StarReviewActivity : BaseActivity<ActivityStarReviewBinding>(R.layout.activity_star_review) {
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
    }

    override fun initObserver() {
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

}