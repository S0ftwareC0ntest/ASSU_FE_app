package com.ssu.assu.presentation.common.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ssu.assu.R
import com.ssu.assu.databinding.ActivityLoginBinding
import com.ssu.assu.presentation.admin.AdminMainActivity
import com.ssu.assu.presentation.base.BaseActivity
import com.ssu.assu.presentation.common.signup.SignUpActivity
import com.ssu.assu.presentation.partner.PartnerMainActivity
import com.ssu.assu.presentation.user.UserMainActivity
import com.ssu.assu.ui.auth.LoginViewModel
import com.ssu.assu.ui.auth.LoginViewModel.LoginState
import com.ssu.assu.ui.deviceToken.DeviceTokenViewModel
import com.ssu.assu.ui.auth.LoginErrorMessageMapper
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val loginViewModel: LoginViewModel by viewModels()
    private var isAutoLoginChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { false }
    }


    override fun initView() {
        setupInitialAnimationState()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val extraPaddingTop = 3
            v.setPadding(
                systemBars.left,
                systemBars.top + extraPaddingTop.dpToPx(v.context),
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        setLoginButtonEnabled(false)
        binding.etLoginId.addTextChangedListener { checkLoginInputValidity() }
        binding.etLoginPassword.addTextChangedListener { checkLoginInputValidity() }

        binding.btnLogin.setOnClickListener {
            if (!binding.btnLogin.isEnabled) return@setOnClickListener

            val email = binding.etLoginId.text.toString().trim()
            val password = binding.etLoginPassword.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginViewModel.commonLogin(email, password)
        }

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.btnLmsLogin.setOnClickListener {
            startActivity(Intent(this, LmsLoginActivity::class.java))
        }
        startLoginAnimation()
    }

    override fun initObserver() {
        // 로그인 상태 관찰
        loginViewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginState.Idle -> Unit
                is LoginState.Loading -> {
                    setLoginButtonEnabled(false)
                    Log.d("LoginActivity", "로그인 중...")
                }
                is LoginState.Success -> {
                    setLoginButtonEnabled(true)
                    Log.d("LoginActivity", "로그인 성공!")
                    // 자동 로그인 체크 플래그 설정하여 중복 실행 방지
                    isAutoLoginChecked = true
                    // 즉시 메인 화면으로 이동 (FCM 토큰 등록은 메인 화면에서 처리)
                    navigateToMainActivity(state.loginData.userRole)
                }
                is LoginState.Error -> {
                    setLoginButtonEnabled(true)
                    // 로그인 전용 에러 메시지 매퍼 사용
                    val errorMessage = LoginErrorMessageMapper.getLoginErrorMessage(state.fail)
                    Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "로그인 실패: code=${state.fail.code}, message=${state.fail.message}")
                }
                is LoginState.PendingApproval -> {
                    setLoginButtonEnabled(true)
                    Toast.makeText(this@LoginActivity, "승인 대기 중입니다: ${state.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        // 자동 로그인 체크 (Observer 설정 후에 호출, 한 번만 실행)
        Log.d("LoginActivity", "=== initObserver - 자동 로그인 체크 준비 ===")
        Log.d("LoginActivity", "isAutoLoginChecked 현재 값: $isAutoLoginChecked")
        if (!isAutoLoginChecked) {
            Log.d("LoginActivity", "자동 로그인 체크 호출")
            checkAutoLogin()
            isAutoLoginChecked = true
        } else {
            Log.d("LoginActivity", "자동 로그인 이미 체크됨 - 건너뛰기")
        }
    }


    private fun navigateToMainActivity(userRole: String) {
        val intent = when (userRole.uppercase()) {
            "ADMIN" -> Intent(this, AdminMainActivity::class.java)
            "PARTNER" -> Intent(this, PartnerMainActivity::class.java)
            else -> Intent(this, UserMainActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        
        // 로그인 액티비티 종료하여 매끄러운 전환
        finish()
    }

    private fun checkLoginInputValidity() {
        val id = binding.etLoginId.text?.toString()?.trim()
        val pw = binding.etLoginPassword.text?.toString()?.trim()
        setLoginButtonEnabled(!id.isNullOrEmpty() && !pw.isNullOrEmpty())
    }

    private fun setLoginButtonEnabled(enabled: Boolean) {
        binding.btnLogin.isEnabled = enabled
        binding.btnLogin.background = ContextCompat.getDrawable(
            this,
            if (enabled) R.drawable.btn_basic_selected else R.drawable.btn_basic_unselected
        )
    }


    private fun checkAutoLogin() {
        Log.d("LoginActivity", "=== 자동 로그인 체크 시작 ===")
        loginViewModel.checkAutoLoginWithRefresh()
    }

    private fun Int.dpToPx(context: Context): Int =
        (this * context.resources.displayMetrics.density).toInt()


    private fun startLoginAnimation() {

        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels.toFloat()

        binding.ivLogo.translationY = screenHeight / 3

        // 나머지 폼 구성요소 리스트 (애니메이션 적용 대상)
        val loginForms = listOf(
            binding.tvLoginEmail, binding.etLoginId,
            binding.tvLoginPassword, binding.etLoginPassword,
            binding.btnLogin, binding.tvLogin, binding.viewLine,
            binding.btnLmsLogin, binding.tvLmsLogin,
            binding.tvSignupGuide, binding.btnSignup
        )

        loginForms.forEach { it.alpha = 0f}

        // 2. 애니메이션 시작
        binding.ivLogo.animate()
            .translationY(0f)
            .setStartDelay(800)
            .setDuration(1000)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                // 로고 이동이 끝나면 나머지 폼들이 순차적으로 스르륵 등장
                loginForms.forEachIndexed { index, view ->
                    view.animate()
                        .alpha(1f)
                        .translationYBy(0f)
                        .setDuration(1000)
                        .setStartDelay(index * 30L)
                        .setInterpolator(DecelerateInterpolator())
                        .start()
                }
            }
            .start()
    }

    private fun setupInitialAnimationState() {
        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels.toFloat()

        binding.ivLogo.translationY = screenHeight / 2

        val loginForms = listOf(
            binding.tvLoginEmail, binding.etLoginId,
            binding.tvLoginPassword, binding.etLoginPassword,
            binding.btnLogin, binding.tvLogin, binding.viewLine,
            binding.btnLmsLogin, binding.tvLmsLogin,
            binding.tvSignupGuide, binding.btnSignup
        )

        loginForms.forEach {
            it.alpha = 0f
            it.translationY = 90f
        }
        startBackgroundSubtleAnimation()
    }

    private fun startBackgroundSubtleAnimation() {
        val scaleX = ObjectAnimator.ofFloat(binding.bgLoginGradation, "scaleX", 1.0f, 1.1f).apply {
            duration = 5000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }
        val scaleY = ObjectAnimator.ofFloat(binding.bgLoginGradation, "scaleY", 1.0f, 1.1f).apply {
            duration = 5000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }

        // 2. 위치 애니메이션 (미세하게 좌우로 이동)
        val translateX = ObjectAnimator.ofFloat(binding.bgLoginGradation, "translationX", -20f, 20f).apply {
            duration = 7000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }

        // 동시에 실행
        AnimatorSet().apply {
            playTogether(scaleX, scaleY, translateX)
            start()
        }
    }

}

