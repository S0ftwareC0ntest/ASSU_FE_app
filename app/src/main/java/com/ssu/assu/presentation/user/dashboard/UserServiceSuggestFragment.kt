package com.ssu.assu.presentation.user.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ssu.assu.R
import com.ssu.assu.databinding.FragmentServiceSuggestDropDownBinding
import com.ssu.assu.databinding.FragmentUserServiceSuggestBinding
import com.ssu.assu.domain.model.suggestion.SuggestionTargetModel
import com.ssu.assu.presentation.base.BaseFragment
import com.ssu.assu.presentation.common.report.OnServiceSuggestListener
import com.ssu.assu.presentation.user.home.temporary.UserServiceSuggestDialogFragment
import com.ssu.assu.presentation.user.home.temporary.UserTemporaryCompleteFragment
import com.ssu.assu.ui.suggestion.SuggestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserServiceSuggestFragment : BaseFragment<FragmentUserServiceSuggestBinding>(R.layout.fragment_user_service_suggest), OnServiceSuggestListener {

    private val viewModel: SuggestionViewModel by activityViewModels()
    private var suggestionTargets: List<SuggestionTargetModel> = emptyList()
    private var dropdownWindow: PopupWindow? = null

    private val ARG_ENTRY_POINT = "entry_point"
    private val ENTRY_QR = "QR_FLOW"

    override fun initView() {
        // 1. Data Binding 설정
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner // Fragment에서는 viewLifecycleOwner 권장

        val entryPoint = arguments?.getString(ARG_ENTRY_POINT)
        if(entryPoint==ENTRY_QR){
            binding.tvSuggestService.visibility = View.GONE
            binding.onlyQaBackArrow.visibility = View.VISIBLE
            binding.tvOnlyQaSuggest.visibility = View.VISIBLE
            binding.onlyQaBackArrow.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        } else{
            binding.tvSuggestService.visibility = View.VISIBLE
            binding.onlyQaBackArrow.visibility = View.GONE
            binding.tvOnlyQaSuggest.visibility = View.GONE
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            // 바텀 네비게이션 뷰가 현재 화면에 보이는지 확인 (Activity에서 가시성 체크)
            val bottomNav = requireActivity().findViewById<View>(R.id.bottom_navigation_view)
            val isBottomNavVisible = bottomNav?.visibility == View.VISIBLE

            // 1. 바텀 네비가 보이면? -> 버튼 아래 마진을 0이나 아주 작게 (이미 네비가 공간을 차지함)
            // 2. 바텀 네비가 안 보이면? -> 버튼 아래에 시스템 네비게이션 바(소프트키)만큼 패딩 추가
            if (isBottomNavVisible) {
                binding.suggestMg5.layoutParams.height = 10.dpToPx(v.context) // 살짝만 띄우기
            } else {
                // 소프트키(navigationBars.bottom) 높이만큼 여백용 뷰의 높이를 조절!
                binding.suggestMg5.layoutParams.height = navigationBars.bottom + 16.dpToPx(v.context)
            }

            insets
        }

        binding.etSuggestWantBenefit.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                // 키보드가 완전히 올라온 후 스크롤되도록 약간의 딜레이를 주거나
                // 뷰의 위치를 계산해서 스크롤합니다.
                binding.nsvSuggest.postDelayed({
                    binding.nsvSuggest.smoothScrollTo(0, view.top) // 입력창 상단이 보이게 하거나
                    // binding.nsvSuggest.fullScroll(View.FOCUS_DOWN) // 혹은 맨 아래로
                }, 200)
            }
        }

        activateCompleteButton()

        binding.spinnerTarget.setOnClickListener {
            if (suggestionTargets.isEmpty()) {
                Toast.makeText(requireContext(), "건의 가능한 대상이 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dropdownWindow?.isShowing == true) {
                dropdownWindow?.dismiss()
            } else {
                showDropdownMenu(it, suggestionTargets)
            }
        }

        binding.btnSuggestComplete.setOnClickListener {
            // TODO : 1학기 임시 운영버전
            temporaryPopUpDialog()
        }
    }

    override fun initObserver() {
        // Fragment에서는 viewLifecycleOwner.lifecycleScope를 사용해야 안전합니다.
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAdminsState.collect { state ->
                    Log.d("SuggestFragment", "getAdminsState changed: $state")
                    when (state) {
                        is SuggestionViewModel.GetAdminsUiState.Success -> {
                            suggestionTargets = state.data
                        }
                        is SuggestionViewModel.GetAdminsUiState.Fail -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        }
                        is SuggestionViewModel.GetAdminsUiState.Error -> {
                            Toast.makeText(requireContext(), "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    private fun activateCompleteButton() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input1 = binding.etSuggestMarket.text.toString().trim()
                val input2 = binding.etSuggestWantBenefit.text.toString().trim()
                val isFilled = input1.isNotEmpty() && input2.isNotEmpty()

                binding.btnSuggestComplete.isEnabled = isFilled
                binding.btnSuggestComplete.setBackgroundResource(
                    if (isFilled) R.drawable.btn_basic_selected else R.drawable.btn_basic_unselected
                )
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etSuggestMarket.addTextChangedListener(textWatcher)
        binding.etSuggestWantBenefit.addTextChangedListener(textWatcher)

        binding.btnSuggestComplete.isEnabled = false
        binding.btnSuggestComplete.setBackgroundResource(R.drawable.btn_basic_unselected)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        dropdownWindow?.dismiss()
    }

    private fun showDropdownMenu(anchor: View, targets: List<SuggestionTargetModel>) {
        dropdownWindow?.dismiss()

        val popupBinding = FragmentServiceSuggestDropDownBinding.inflate(layoutInflater)
        val popupWindow = PopupWindow(
            popupBinding.root,
            anchor.width,
            WRAP_CONTENT,
            true
        ).apply {
            elevation = 10f
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        popupWindow.setOnDismissListener { dropdownWindow = null }

        val textViews = listOf(
            popupBinding.tvSuggestDropTarget1,
            popupBinding.tvSuggestDropTarget2,
            popupBinding.tvSuggestDropTarget3
        )
        val dividers = listOf(popupBinding.lineDivide1, popupBinding.lineDivide2)

        textViews.forEach { it.visibility = View.GONE }
        dividers.forEach { it.visibility = View.GONE }

        targets.forEachIndexed { index, target ->
            if (index < textViews.size) {
                textViews[index].apply {
                    visibility = View.VISIBLE
                    text = target.name
                    setOnClickListener {
                        viewModel.selectTarget(target)
                        popupWindow.dismiss()
                    }
                }
                if (index < targets.size - 1 && index < dividers.size) {
                    dividers[index].visibility = View.VISIBLE
                }
            }
        }

        popupWindow.showAsDropDown(anchor, -5, -155)
        this.dropdownWindow = popupWindow
    }

    private fun temporaryPopUpDialog() {
        // childFragmentManager를 사용하는 것이 Fragment 내의 Dialog 관리에 적합합니다.
        UserServiceSuggestDialogFragment().show(childFragmentManager, "SuggestDialog")
    }

    override fun onServiceSuggest() {
        val entryPoint = arguments?.getString(ARG_ENTRY_POINT)
        viewModel.writeSuggestion()
        viewModel.insertTemporaryQrData("SUGGEST")

        // Activity의 setResult 및 finish 호출
        if (entryPoint == ENTRY_QR) {
            // 1. QR 인증을 통해 들어온 경우: 결과 전달 후 액티비티 종료 (완료 화면으로 이동)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, UserTemporaryCompleteFragment())
                .commit()
        } else {
            Toast.makeText(requireContext(), "건의가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            resetInputs()
            Log.d("SuggestFragment", "일반 진입 플로우 - 화면 유지")
        }
    }

    private fun resetInputs() {
        viewModel.clearInputs()
    }

}