package com.ssu.assu.presentation.user.home.temporary

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.ssu.assu.R
import com.ssu.assu.databinding.FragmentUserEventSelectBinding
import com.ssu.assu.presentation.base.BaseFragment
import com.ssu.assu.presentation.user.dashboard.UserServiceSuggestActivity

class UserEventSelectFragment : BaseFragment<FragmentUserEventSelectBinding>(R.layout.fragment_user_event_select) {
private val startActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    Log.d("FragmentB", "결과 코드: ${result.resultCode}") // -1이 찍히면 정상!
    if (result.resultCode == Activity.RESULT_OK) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, UserTemporaryCompleteFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss() // 상태 손실을 허용하며 즉시 반영
    }
}
    private var selectedIndex: Int? = null
    private lateinit var eventButtons: List<View>
    override fun initObserver() {

    }

    override fun initView() {
        eventButtons = listOf(
            binding.btnEventSelect1,
            binding.btnEventSelect2,
            binding.btnEventSelect3
        )

        eventButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                updateSelection(index)
            }
        }

        binding.btnSelectEventComplete.setOnClickListener {
            when (selectedIndex) {
                0 -> toAssuReviewWrite()    // 첫 번째 버튼 선택 시
                1 -> toSuggestPartnership()  // 두 번째 버튼 선택 시
                2 -> toComplete()           // 세 번째 버튼 선택 시
                else -> {
                    // 아무것도 선택 안 됐을 때 처리 (필요시)
                }
            }
        }

        // 초기 상태 (선택 안 됨)
        updateSelection(-1)


    }

    private fun updateSelection(selected: Int) {
        selectedIndex = selected

        eventButtons.forEachIndexed { index, layout ->
            val isSelected = (index == selected)

            // 선택 상태에 따른 UI 업데이트
            updateButtonAppearance(layout, index, isSelected)
        }

        // 선택 완료 버튼 활성화
        updateCompleteButtonState(true)
    }

    private fun updateButtonAppearance(layout: View, index: Int, isSelected: Boolean) {
        // 배경 변경
        layout.setBackgroundResource(
            if (isSelected) R.drawable.bg_partnership_selected
            else R.drawable.bg_partnership_unselected
        )

        // 투명도 변경
        layout.alpha = if (isSelected) 1.0f else 0.5f

        // 텍스트 색상 변경
        val titleText = layout.findViewById<TextView>(
            when(index) {
                0 -> R.id.tv_event_1_title
                1 -> R.id.tv_event_select_2_title
                2 -> R.id.tv_event_select_3_title
                else -> return
            }
        )


        val color = if (isSelected) R.color.assu_main else R.color.assu_font_main
        titleText?.setTextColor(ContextCompat.getColor(requireContext(), color))
    }

    private fun updateCompleteButtonState(isEnabled: Boolean) {
        binding.btnSelectEventComplete.apply {
            this.isEnabled = isEnabled
            setBackgroundResource(
                if (isEnabled) R.drawable.btn_basic_selected
                else R.drawable.btn_basic_unselected
            )
        }
    }

    private fun toAssuReviewWrite(){

    }

    private fun toSuggestPartnership(){
        val intent = Intent(requireContext(), UserServiceSuggestActivity::class.java)
        intent.apply {
            putExtra("demo" , "EventSelectFragment")
        }
        startActivity.launch(intent)
    }

    private fun toComplete(){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, UserNextTimeFragment()).commit()

    }

}