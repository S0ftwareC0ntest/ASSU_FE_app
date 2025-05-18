package com.example.assu_fe_app.presentation.signup

import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentSignUpCommonVerifyBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import android.widget.Toast

class SignUpCommonVerifyFragment :
    BaseFragment<FragmentSignUpCommonVerifyBinding>(R.layout.fragment_sign_up_common_verify) {

    private var countDownTimer: CountDownTimer? = null
    private val totalTimeMillis = 5 * 60 * 1000L // 5분
    private var isVerified = false
    // 임시 허용
    private val correctPhoneNumber = "01012345678"
    private val correctVerificationCode = "1234"

    override fun initObserver() {}

    override fun initView() {
        // 인증번호 받기
        binding.tvUserVerifyPhone.setOnClickListener {
            val inputPhone = binding.etUserVerifyPhone.text.toString()
            if (inputPhone == correctPhoneNumber) {
                startVerificationUI()
                startTimer()
            } else {
                Toast.makeText(requireContext(), "올바르지 않은 전화번호입니다", Toast.LENGTH_SHORT).show()
            }
        }

        // 엔터 입력 시 인증 처리
        binding.etUserVerifyCode.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                checkVerificationCode()
                true
            } else {
                false
            }
        }

        // 인증 완료 버튼 클릭 → 다음 프래그먼트 이동
        binding.btnCompleted.setOnClickListener {
            if (isVerified) {
                findNavController().navigate(R.id.action_verify_to_account)
            }
        }
    }

    private fun startVerificationUI() {
        // 전화번호 필드 비활성화
        binding.etUserVerifyPhone.isEnabled = false

        // UI 변경
        binding.ivUserVerifyCheckIcon.isVisible = true
        binding.tvUserVerifyPhone.text = "전송완료"

        binding.clUserVerifyCode.visibility = View.VISIBLE
        binding.llQuestionCodeIsNotComing.visibility = View.VISIBLE
        binding.tvUserVerifyCode.text = "05:00"

        // 기본 상태 초기화
        binding.etUserVerifyCode.text?.clear()
        binding.etUserVerifyCode.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_signup_input_bar)
        binding.ivUserVerifyCodeCheckIcon.visibility = View.GONE
        binding.tvUserVerifyCode.setTextColor(ContextCompat.getColor(requireContext(), R.color.assu_main))
        binding.tvUserVerifyCode.text = "05:00"

        setButtonEnabled(false)
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(totalTimeMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.tvUserVerifyCode.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                Toast.makeText(requireContext(), "인증 시간이 만료되었습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                resetUI()
            }
        }.start()
    }

    private fun checkVerificationCode() {
        val enteredCode = binding.etUserVerifyCode.text.toString().trim()
        val correctCode = "1234"

        // 키보드 내리기
        val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(binding.etUserVerifyCode.windowToken, 0)

        if (enteredCode == correctCode) {
            successVerificationUI()
        } else {
            errorVerificationUI()
        }
    }

    private fun successVerificationUI() {
        countDownTimer?.cancel()
        isVerified = true

        // 타이머 숨기기 + 인증 완료 표시
        binding.tvUserVerifyCode.text = "인증완료"
        binding.tvUserVerifyCode.setTextColor(ContextCompat.getColor(requireContext(), R.color.assu_main))
        binding.ivUserVerifyCodeCheckIcon.setImageResource(R.drawable.ic_signup_verified)
        binding.ivUserVerifyCodeCheckIcon.visibility = View.VISIBLE
        binding.clUserVerifyCode.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_signup_input_bar_selected)

        // 버튼 활성화
        setButtonEnabled(true)
    }

    private fun errorVerificationUI() {
        countDownTimer?.cancel()

        binding.tvUserVerifyCode.text = "인증오류"
        binding.tvUserVerifyCode.setTextColor(ContextCompat.getColor(requireContext(), R.color.assu_error))
        binding.ivUserVerifyCodeCheckIcon.setImageResource(R.drawable.ic_signup_verified_failed)
        binding.ivUserVerifyCodeCheckIcon.visibility = View.VISIBLE
        binding.clUserVerifyCode.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_signup_input_bar_error)

        // 버튼 비활성화
        setButtonEnabled(false)
    }

    private fun setButtonEnabled(enabled: Boolean) {
        binding.btnCompleted.isEnabled = enabled
        binding.btnCompleted.background = ContextCompat.getDrawable(
            requireContext(),
            if (enabled) R.drawable.btn_basic_selected else R.drawable.btn_basic_unselected
        )
    }

    private fun resetUI() {
        isVerified = false
        binding.etUserVerifyPhone.isEnabled = true
        binding.etUserVerifyPhone.text?.clear()
        binding.etUserVerifyCode.text?.clear()
        binding.ivUserVerifyCheckIcon.isVisible = false
        binding.tvUserVerifyPhone.text = "인증번호 받기"
        binding.clUserVerifyCode.visibility = View.GONE
        binding.llQuestionCodeIsNotComing.visibility = View.GONE
        setButtonEnabled(false)
    }

    override fun onDestroyView() {
        countDownTimer?.cancel()
        super.onDestroyView()
    }
}
