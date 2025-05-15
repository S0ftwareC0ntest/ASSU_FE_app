package com.example.assu_fe_app.presentation.location

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivityPhotoReviewBinding
import com.example.assu_fe_app.presentation.base.BaseActivity

class PhotoReviewActivity : BaseActivity<ActivityPhotoReviewBinding>(R.layout.activity_photo_review) {
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
    }

    override fun initObserver() {
    }


    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}