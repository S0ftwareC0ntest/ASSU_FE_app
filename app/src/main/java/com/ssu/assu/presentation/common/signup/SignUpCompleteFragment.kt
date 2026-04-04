package com.ssu.assu.presentation.common.signup

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ssu.assu.R
import com.ssu.assu.databinding.FragmentSignUpCompleteBinding
import com.ssu.assu.presentation.base.BaseFragment
import com.ssu.assu.presentation.common.login.LoginActivity
import com.ssu.assu.presentation.user.UserMainActivity
import com.ssu.assu.presentation.admin.AdminMainActivity
import com.ssu.assu.presentation.partner.PartnerMainActivity
import com.ssu.assu.ui.auth.SignUpViewModel
import kotlinx.coroutines.launch

class SignUpCompleteFragment : BaseFragment<FragmentSignUpCompleteBinding>(R.layout.fragment_sign_up_complete){
    
    private val signUpViewModel: SignUpViewModel by activityViewModels()

    override fun initObserver() {
        // isLoading 초기값이 false라서, 로딩이 한 번이라도 true가 된 뒤 false로 돌아올 때만 결과를 판별한다.
        // 에러 토스트·로그인 복귀도 여기서만 처리해 errorMessage collect와의 레이스를 막는다.
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                var wasLoading = false
                signUpViewModel.isLoading.collect { isLoading ->
                    Log.d("SignUpCompleteFragment", "로딩 상태 변경: $isLoading")
                    binding.loadingOverlay.visibility =
                        if (isLoading) View.VISIBLE else View.GONE
                    if (isLoading) {
                        binding.tvLoadingText.setText(R.string.signup_loading_message)
                        wasLoading = true
                    } else if (wasLoading) {
                        val result = signUpViewModel.signUpResult.value
                        val errorMessage = signUpViewModel.errorMessage.value

                        Log.d("SignUpCompleteFragment", "=== 회원가입 요청 종료 후 상태 ===")
                        Log.d("SignUpCompleteFragment", "회원가입 결과: $result")
                        Log.d("SignUpCompleteFragment", "에러 메시지: $errorMessage")

                        when {
                            result != null -> {
                                Log.d("SignUpCompleteFragment", "회원가입 성공: $result")
                                applySignupSuccessWelcome()
                            }
                            errorMessage != null -> {
                                Log.e("SignUpCompleteFragment", "회원가입 실패(에러): $errorMessage")
                                signUpViewModel.consumeExitToLoginAfterError()
                                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                                signUpViewModel.clearError()
                                navigateToLogin()
                            }
                            else -> {
                                Log.e("SignUpCompleteFragment", "회원가입 실패: 결과·에러 없음")
                                Toast.makeText(
                                    requireContext(),
                                    "회원가입에 실패했습니다. 다시 시도해주세요.",
                                    Toast.LENGTH_LONG
                                ).show()
                                navigateToLogin()
                            }
                        }
                        wasLoading = false
                    }
                }
            }
        }
    }

    override fun initView() {
        // 학생 플로우는 확인 화면에서 이미 회원가입 API가 성공한 뒤 진입할 수 있음
        if (signUpViewModel.signUpResult.value != null) {
            applySignupSuccessWelcome()
        } else {
            signUpViewModel.signUp()
        }

        // 회원가입 완료 후 사용자 타입에 따른 Main Activity로 이동
        binding.btnCompleted.setOnClickListener {
            navigateToMainActivity()
        }
    }

    private fun applySignupSuccessWelcome() {
        val result = signUpViewModel.signUpResult.value ?: return
        val userName = result.basicInfo?.name ?: result.username
        val welcomeText = getString(R.string.signup_welcome_format, userName)
        binding.tvSignupDoneUsername.text = welcomeText
    }

    // 사용자 타입에 따른 Main Activity로 이동하는 함수
    private fun navigateToMainActivity() {
        val userType = signUpViewModel.signUpData.value.userType
        
        val intent = when (userType) {
            "admin" -> Intent(requireContext(), AdminMainActivity::class.java)
            "partner" -> Intent(requireContext(), PartnerMainActivity::class.java)
            "user" -> {
                // 학생 계정의 경우 UserMainActivity로 이동하되 학생 탭으로 이동하도록 설정
                val studentIntent = Intent(requireContext(), UserMainActivity::class.java)
                studentIntent.putExtra("nav_dest_id", R.id.dashboardFragment) // 학생은 대시보드 탭으로 이동
                studentIntent
            }
            else -> Intent(requireContext(), UserMainActivity::class.java)
        }
        
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    // LoginActivity로 돌아가는 함수
    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }
}