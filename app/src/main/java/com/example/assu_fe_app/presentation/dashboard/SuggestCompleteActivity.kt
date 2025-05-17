package com.example.assu_fe_app.presentation.dashboard

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivitySuggestCompleteBinding
import com.example.assu_fe_app.presentation.base.BaseActivity

class SuggestCompleteActivity : BaseActivity<ActivitySuggestCompleteBinding>(R.layout.activity_suggest_complete){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_suggest_complete)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun initView() {

    }

    override fun initObserver() {

    }
}