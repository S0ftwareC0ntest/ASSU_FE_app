package com.example.assu_fe_app.presentation.partner.dashboard

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentPartnerDashboardBinding
import com.example.assu_fe_app.presentation.base.BaseFragment

class PartnerDashboardFragment :
    BaseFragment<FragmentPartnerDashboardBinding>(R.layout.fragment_partner_dashboard) {

    override fun initObserver() {}

    override fun initView() {
        // 예시 적용
        setClientAnalysisText(
            fullText = "제휴 이용자를 통해\n매출의 14%를 기록하고 있어요",
            highlightText = "14%"
        )

        binding.btnViewContract.setOnClickListener {
            findNavController().navigate(R.id.action_partner_dashboard_to_partner_review)
        }
    }

    // 분석 문구 중 특정 텍스트에 색상을 입히는 함수
    private fun setClientAnalysisText(fullText: String, highlightText: String) {
        val spannable = SpannableString(fullText)
        val start = fullText.indexOf(highlightText)
        if (start >= 0) {
            val end = start + highlightText.length
            val color = ContextCompat.getColor(requireContext(), R.color.assu_main)
            spannable.setSpan(
                ForegroundColorSpan(color),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.tvDashboardClientAnalysis.text = spannable
    }
}