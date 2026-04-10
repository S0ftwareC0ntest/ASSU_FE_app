package com.ssu.assu.presentation.user.signup

import android.content.Intent
import android.content.res.ColorStateList
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ssu.assu.R
import com.ssu.assu.data.dto.auth.StudentTokenVerifyResponseDto
import com.ssu.assu.databinding.FragmentUserSignUpStudentCheckBinding
import com.ssu.assu.presentation.base.BaseFragment
import com.ssu.assu.presentation.common.login.LoginActivity
import com.ssu.assu.presentation.user.mypage.UserMypagePrivacyDialogFragment
import com.ssu.assu.ui.auth.SignUpViewModel
import com.ssu.assu.util.setProgressBarFillAnimated
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserSignUpStudentCheckFragment : 
    BaseFragment<FragmentUserSignUpStudentCheckBinding>(R.layout.fragment_user_sign_up_student_check) {

    private val signUpViewModel: SignUpViewModel by activityViewModels()

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    signUpViewModel.studentVerifyResult.collect { result ->
                        result?.let {
                            applyVerifiedStudentFields(it)
                            signUpViewModel.clearStudentVerifyResult()
                        }
                    }
                }
                launch {
                    signUpViewModel.isLoading.collect { loading ->
                        binding.loadingOverlay.visibility =
                            if (loading) View.VISIBLE else View.GONE
                        if (loading) {
                            binding.tvLoadingText.setText(R.string.signup_loading_message)
                        }
                        updateButtonState()
                    }
                }
            }
        }
    }

    override fun initView() {
        binding.ivSignupProgressBar.setProgressBarFillAnimated(
            container = binding.flSignupProgressContainer,
            fromPercent = 0.70f,
            toPercent = 0.85f
        )

        // "LMS 인증" 부분을 assu_main 컬러로 설정
        binding.tvSchoolAccountCheckTitle.text = buildSpannedString {
            color(ContextCompat.getColor(requireContext(), R.color.assu_main)) {
                append("LMS 인증")
            }
            append("에 성공했어요!\n학부와 학번을 확인해주세요!")
        }

        // 체크박스 색상 설정
        setupCheckboxColors()

        // 체크박스 리스너 설정
        setupCheckboxListeners()

        // 개인정보 처리방침 링크 클릭
        binding.tvPrivacyLink.setOnClickListener {
            showPrivacyDialog()
        }

        // 서비스 이용약관 링크 클릭
        binding.tvTermsLink.setOnClickListener {
            showTermsDialog()
        }

        // 완료 버튼 클릭 — 회원가입 API 성공 후에만 완료 화면으로 이동
        binding.btnCompleted.setOnClickListener {
            if (signUpViewModel.signUpResult.value != null) {
                findNavController().navigate(R.id.action_user_student_check_to_complete)
                return@setOnClickListener
            }
            val analytics = FirebaseAnalytics.getInstance(requireContext())
            analytics.setUserProperty("user_type", "student")

            viewLifecycleOwner.lifecycleScope.launch {
                signUpViewModel.signUp()
                signUpViewModel.isLoading.first { it }
                signUpViewModel.isLoading.first { !it }
                if (!isAdded) return@launch

                when {
                    signUpViewModel.signUpResult.value != null ->
                        findNavController().navigate(R.id.action_user_student_check_to_complete)
                    signUpViewModel.errorMessage.value != null -> {
                        val msg = signUpViewModel.errorMessage.value
                        signUpViewModel.consumeExitToLoginAfterError()
                        Toast.makeText(requireContext(), msg ?: "", Toast.LENGTH_LONG).show()
                        signUpViewModel.clearError()
                        navigateToLogin()
                    }
                    else -> {
                        Toast.makeText(
                            requireContext(),
                            "회원가입에 실패했습니다. 다시 시도해주세요.",
                            Toast.LENGTH_LONG
                        ).show()
                        navigateToLogin()
                    }
                }
            }
        }
        
        signUpViewModel.studentVerifyResult.value?.let { applyVerifiedStudentFields(it) }

        // 초기 버튼 상태 설정
        updateButtonState()
    }

    /** 서버가 전공명을 한글 그대로 내려주므로 그대로 표시한다. */
    private fun applyVerifiedStudentFields(dto: StudentTokenVerifyResponseDto) {
        val majorText = dto.major.trim().ifEmpty { "학과 정보 없음" }
        binding.etStudentMajor.setText(majorText)
        binding.etStudentId.setText(dto.studentNumber)
    }
    
    private fun updateButtonState() {
        val isPrivacyAgreed = binding.cbPrivacyAgree.isChecked
        val isSignupLoading = signUpViewModel.isLoading.value

        // 필수 약관(개인정보 처리방침 + 서비스 이용약관)이 체크되어야 버튼 활성화
        // 선택 약관(cbMarketingAgree)은 체크 여부와 관계없이 진행 가능
        val isButtonEnabled = isPrivacyAgreed && !isSignupLoading
        
        binding.btnCompleted.isEnabled = isButtonEnabled
        binding.btnCompleted.background = ContextCompat.getDrawable(
            requireContext(),
            if (isButtonEnabled) R.drawable.btn_basic_selected else R.drawable.btn_basic_unselected
        )
    }

    private fun showPrivacyDialog() {
        val dialog = UserMypagePrivacyDialogFragment()
        dialog.show(parentFragmentManager, "PrivacyDialog")
    }

    private fun showTermsDialog() {
        val dialog = UserSignUpTermsDialogFragment()
        dialog.show(parentFragmentManager, "TermsDialog")
    }

    private fun setAllAgreement(isChecked: Boolean) {
        // 전체 동의 체크박스의 리스너를 일시적으로 비활성화
        binding.cbAllAgree.setOnCheckedChangeListener(null)
        binding.cbPrivacyAgree.setOnCheckedChangeListener(null)
        binding.cbMarketingAgree.setOnCheckedChangeListener(null)

        // 모든 체크박스 상태 설정
        binding.cbAllAgree.isChecked = isChecked
        binding.cbPrivacyAgree.isChecked = isChecked
        binding.cbMarketingAgree.isChecked = isChecked

        // ViewModel에 상태 저장
        signUpViewModel.setLocationAgree(isChecked) // 필수 약관 (개인정보처리방침 + 위치정보 수집동의)
        signUpViewModel.setMarketingAgree(isChecked) // 선택 약관 (Email 및 SMS 마케팅 수신 동의)

        // 리스너 재설정
        setupCheckboxListeners()
    }

    private fun updateAllAgreeState() {
        val isPrivacyAgreed = binding.cbPrivacyAgree.isChecked
        val isTermsAgreed = binding.cbMarketingAgree.isChecked

        // 모든 개별 약관이 체크되어 있으면 전체 동의도 체크
        val allChecked = isPrivacyAgreed && isTermsAgreed
        
        // 전체 동의 체크박스의 리스너를 일시적으로 비활성화
        binding.cbAllAgree.setOnCheckedChangeListener(null)
        binding.cbAllAgree.isChecked = allChecked
        // 리스너 재설정
        binding.cbAllAgree.setOnCheckedChangeListener { _, isChecked ->
            setAllAgreement(isChecked)
            updateButtonState()
        }
    }

    private fun setupCheckboxListeners() {
        // 전체 동의 체크박스 리스너 설정
        binding.cbAllAgree.setOnCheckedChangeListener { _, isChecked ->
            setAllAgreement(isChecked)
            updateButtonState()
        }

        // 개별 약관 체크박스 리스너 설정
        binding.cbPrivacyAgree.setOnCheckedChangeListener { _, isChecked ->
            signUpViewModel.setLocationAgree(isChecked) // 필수 약관 (개인정보처리방침 + 위치정보 수집동의)
            updateAllAgreeState()
            updateButtonState()
        }

        binding.cbMarketingAgree.setOnCheckedChangeListener { _, isChecked ->
            signUpViewModel.setMarketingAgree(isChecked) // 선택 약관 (Email 및 SMS 마케팅 수신 동의)
            updateAllAgreeState()
            updateButtonState()
        }
    }

    // 체크박스 색상 설정
    private fun setupCheckboxColors() {
        val assuMainColor = ContextCompat.getColor(requireContext(), R.color.assu_main)
        val assuFontSubColor = ContextCompat.getColor(requireContext(), R.color.assu_font_sub)
        
        // 체크된 상태와 체크되지 않은 상태의 색상 설정
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(assuMainColor, assuFontSubColor)
        )
        
        // 모든 체크박스에 색상 적용
        binding.cbAllAgree.buttonTintList = colorStateList
        binding.cbPrivacyAgree.buttonTintList = colorStateList
        binding.cbMarketingAgree.buttonTintList = colorStateList
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }
}
