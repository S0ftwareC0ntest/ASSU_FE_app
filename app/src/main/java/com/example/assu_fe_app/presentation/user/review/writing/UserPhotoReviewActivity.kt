package com.example.assu_fe_app.presentation.user.review.writing

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivityUserPhotoReviewBinding
import com.example.assu_fe_app.presentation.base.BaseActivity

class UserPhotoReviewActivity : BaseActivity<ActivityUserPhotoReviewBinding>(R.layout.activity_user_photo_review) {
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

        val backButton = binding.ivMyPhotoReviewBackArrow
        backButton.setOnClickListener{
            finish() // StarReviewActivity로 돌아감
        }

        val finishWritingDeactivatedButton = binding.layoutFinishReviewDeactivatedButton
        val finishWritingActivatedButton = binding.layoutFinishReviewActivatedButton
        var review = binding.etWritePhotoReview

        review.addTextChangedListener {
            val text = it.toString()
            if(text.isNotBlank()) {
                finishWritingDeactivatedButton.visibility = View.GONE
                finishWritingActivatedButton.visibility = View.VISIBLE
            } else {
                finishWritingDeactivatedButton.visibility = View.VISIBLE
                finishWritingActivatedButton.visibility = View.GONE
            }
        }

        finishWritingActivatedButton.setOnClickListener {
            val intent = Intent(this, UserFinishReviewActivity::class.java)
            startActivity(intent)
        }

    }

    override fun initObserver() {
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}