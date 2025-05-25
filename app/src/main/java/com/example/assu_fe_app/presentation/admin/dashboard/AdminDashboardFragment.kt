package com.example.assu_fe_app.presentation.admin.dashboard

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentAdminDashboardBinding
import com.example.assu_fe_app.presentation.base.BaseFragment

class AdminDashboardFragment :
    BaseFragment<FragmentAdminDashboardBinding>(R.layout.fragment_admin_dashboard) {

    override fun initObserver() {
    }

    override fun initView() {
        setPredictionText(192)

        binding.btnViewSuggestions.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_admin_dashboard_to_suggestions)
        }
    }

    private fun setPredictionText(count: Int) {
        val fullText = "이번달에는 ${count}건 이상일 것으로 예상돼요"
        val spannable = SpannableString(fullText)

        val start = fullText.indexOf("$count")
        val end = start + "$count".length

        val color = ContextCompat.getColor(requireContext(), R.color.assu_main)
        spannable.setSpan(
            ForegroundColorSpan(color),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvDashboardPrediction.text = spannable
    }
}