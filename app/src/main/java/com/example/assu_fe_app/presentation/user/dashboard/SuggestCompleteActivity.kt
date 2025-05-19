package com.example.assu_fe_app.presentation.user.dashboard

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivitySuggestCompleteBinding
import com.example.assu_fe_app.presentation.base.BaseActivity

class SuggestCompleteActivity : BaseActivity<ActivitySuggestCompleteBinding>(R.layout.activity_suggest_complete){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

    }

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


        binding.btnCompleteCancel.setOnClickListener {
            finish()
        }
    }

    override fun initObserver() {

    }
    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}