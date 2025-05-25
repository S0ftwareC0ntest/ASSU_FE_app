package com.example.assu_fe_app.presentation.user.review.writing

import android.content.Context
import android.content.Intent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivityFinishReviewBinding
import com.example.assu_fe_app.presentation.user.UserMainActivity
import com.example.assu_fe_app.presentation.base.BaseActivity

class UserFinishReviewActivity : BaseActivity<ActivityFinishReviewBinding>(R.layout.activity_finish_review) {
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

        val crossButton = binding.ivCross
        crossButton.setOnClickListener {
            navigateToHome()
        }


    }

    override fun initObserver() {
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    private fun navigateToHome() {
        val intent = Intent(this, UserMainActivity::class.java).apply {
            // 기존 Task 스택 위로 올라가서 중복 생성 방지
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            // BottomNavigationView에 전달할 목적지 ID
            putExtra("R.id.homeFragment", R.id.homeFragment)
        }
        startActivity(intent)
        finish() // FinishReviewActivity 종료
    }


}