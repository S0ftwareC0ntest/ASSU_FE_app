package com.example.assu_fe_app.presentation.admin.dashboard

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivityAdminDashboardSuggestionsBinding
import com.example.assu_fe_app.presentation.admin.dashboard.adapter.AdminSuggestionItem
import com.example.assu_fe_app.presentation.admin.dashboard.adapter.AdminSuggestionListAdapter
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

        binding.ivSuggestionListBack.setOnClickListener {
            finish() // 현재 Activity 종료 → 이전 화면으로 돌아감
        }

        val dummyList = listOf(
            AdminSuggestionItem(
                departmentInfo = "글로벌미디어학부 20231616",
                status = "재학중",
                content = "제휴 이벤트 잘 이용하고 있어요!!! 취창이나 인생맥주 제휴 희망합니다 👍🏻",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "소프트웨어학부 20214545",
                status = "재학중",
                content = "무지하게 잘 먹었습니다! 제휴 이벤트 좋았어요! 그런데 4인 이상이라는 조건이 없으면, 더 좋을거 같아요!!",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "언론홍보학과 20214545",
                status = "휴학중",
                content = "무지하게 잘 먹었습니다! 제휴 이벤트 좋았어요!",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "글로벌미디어학부 20231616",
                status = "재학중",
                content = "제휴 이벤트 잘 이용하고 있어요!!! 취창이나 인생맥주 제휴 희망합니다 👍🏻",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "소프트웨어학부 20214545",
                status = "재학중",
                content = "무지하게 잘 먹었습니다! 제휴 이벤트 좋았어요! 그런데 4인 이상이라는 조건이 없으면, 더 좋을거 같아요!!",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "언론홍보학과 20214545",
                status = "휴학중",
                content = "무지하게 잘 먹었습니다! 제휴 이벤트 좋았어요!",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "글로벌미디어학부 20231616",
                status = "재학중",
                content = "제휴 이벤트 잘 이용하고 있어요!!! 취창이나 인생맥주 제휴 희망합니다 👍🏻",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "소프트웨어학부 20214545",
                status = "재학중",
                content = "무지하게 잘 먹었습니다! 제휴 이벤트 좋았어요! 그런데 4인 이상이라는 조건이 없으면, 더 좋을거 같아요!!",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "언론홍보학과 20214545",
                status = "휴학중",
                content = "무지하게 잘 먹었습니다! 제휴 이벤트 좋았어요!",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "글로벌미디어학부 20231616",
                status = "재학중",
                content = "제휴 이벤트 잘 이용하고 있어요!!! 취창이나 인생맥주 제휴 희망합니다 👍🏻",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "소프트웨어학부 20214545",
                status = "재학중",
                content = "무지하게 잘 먹었습니다! 제휴 이벤트 좋았어요! 그런데 4인 이상이라는 조건이 없으면, 더 좋을거 같아요!!",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "언론홍보학과 20214545",
                status = "휴학중",
                content = "무지하게 잘 먹었습니다! 제휴 이벤트 좋았어요!",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "글로벌미디어학부 20231616",
                status = "재학중",
                content = "제휴 이벤트 잘 이용하고 있어요!!! 취창이나 인생맥주 제휴 희망합니다 👍🏻",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "소프트웨어학부 20214545",
                status = "재학중",
                content = "무지하게 잘 먹었습니다! 제휴 이벤트 좋았어요! 그런데 4인 이상이라는 조건이 없으면, 더 좋을거 같아요!!",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "언론홍보학과 20214545",
                status = "휴학중",
                content = "무지하게 잘 먹었습니다! 제휴 이벤트 좋았어요!",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "글로벌미디어학부 20231616",
                status = "재학중",
                content = "제휴 이벤트 잘 이용하고 있어요!!! 취창이나 인생맥주 제휴 희망합니다 👍🏻",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "소프트웨어학부 20214545",
                status = "재학중",
                content = "무지하게 잘 먹었습니다! 제휴 이벤트 좋았어요! 그런데 4인 이상이라는 조건이 없으면, 더 좋을거 같아요!!",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "언론홍보학과 20214545",
                status = "휴학중",
                content = "무지하게 잘 먹었습니다! 제휴 이벤트 좋았어요!",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "글로벌미디어학부 20231616",
                status = "재학중",
                content = "제휴 이벤트 잘 이용하고 있어요!!! 취창이나 인생맥주 제휴 희망합니다 👍🏻",
                date = "2025-03-15 18:36"
            ),
            AdminSuggestionItem(
                departmentInfo = "소프트웨어학부 20214545",
                status = "재학중",
                content = "무지하게 잘 먹었습니다! 제휴 이벤트 좋았어요! 그런데 4인 이상이라는 조건이 없으면, 더 좋을거 같아요!!",
                date = "2025-03-15 18:36"
            )
        )

        val adapter = AdminSuggestionListAdapter(dummyList)
        binding.rvSuggestionList.adapter = adapter
        binding.rvSuggestionList.layoutManager = LinearLayoutManager(this)

        // 아이템 간 여백 설정 (20dp)
        binding.rvSuggestionList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                if (position != 0) {
                    outRect.top = (20 * resources.displayMetrics.density).toInt()
                }
            }
        })
    }

    override fun initObserver() {
        // 옵저버 필요한 경우 작성
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}