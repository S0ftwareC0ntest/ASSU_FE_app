package com.example.assu_fe_app.presentation.admin.dashboard

import android.content.Context
import android.content.Intent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivityAdminDashboardSuggestionsBinding
import com.example.assu_fe_app.presentation.base.BaseActivity

class AdminDashboardSuggestionsActivity : BaseActivity<ActivityAdminDashboardSuggestionsBinding>(R.layout.activity_admin_dashboard_suggestions) {

    override fun initView() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val extraPaddingTop = 3 // 8dp 추가
            v.setPadding(
                systemBars.left,
                systemBars.top + extraPaddingTop.dpToPx(v.context),
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    override fun initObserver() {
        // 옵저버 필요한 경우 작성
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}