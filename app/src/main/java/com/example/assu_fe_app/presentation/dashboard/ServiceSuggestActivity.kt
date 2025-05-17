package com.example.assu_fe_app.presentation.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivityServiceSuggestBinding
import com.example.assu_fe_app.presentation.base.BaseActivity

class ServiceSuggestActivity : BaseActivity<ActivityServiceSuggestBinding>(R.layout.activity_service_suggest){

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

        // 뒤로가기 버튼
        binding.btnSuggestBack.setOnClickListener {
            finish()
        }

        binding.btnSuggestComplete.setOnClickListener {

        }
    }

    override fun initObserver() {

    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}