package com.example.assu_fe_app.presentation.common.login

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivityLoginBinding
import com.example.assu_fe_app.presentation.admin.AdminMainActivity
import com.example.assu_fe_app.presentation.base.BaseActivity
import com.example.assu_fe_app.presentation.common.signup.SignUpActivity
import com.example.assu_fe_app.presentation.partner.PartnerMainActivity
import com.example.assu_fe_app.presentation.user.UserMainActivity
import com.example.assu_fe_app.ui.deviceToken.DeviceTokenViewModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val deviceTokenViewModel: DeviceTokenViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun initView() {
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

        // 자동 로그인 체크
        checkAutoLogin()

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
                    navigateToMainActivity(state.loginData.userRole)
                }
                is LoginState.Error -> {
                    setLoginButtonEnabled(true)
                    Toast.makeText(this, "로그인 실패: ${state.message}", Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "로그인 실패: ${state.message}")
                }
                is LoginState.PendingApproval -> {
                    setLoginButtonEnabled(true)
                    Toast.makeText(this, "승인 대기 중입니다: ${state.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // FCM 토큰 등록 상태 관찰
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                deviceTokenViewModel.uiState.collect { state ->
                    when (state) {
                        is DeviceTokenViewModel.UiState.Idle -> Unit
                        is DeviceTokenViewModel.UiState.Loading -> {
                            Log.d("FCM", "디바이스 토큰 등록 중…")
                        }
                        is DeviceTokenViewModel.UiState.Success -> {
                            Toast.makeText(this@LoginActivity, "푸시 등록 완료", Toast.LENGTH_SHORT).show()
                            Log.i("FCM", "등록 성공: ${state.msg}")
                        }
                        is DeviceTokenViewModel.UiState.Fail -> {
                            Toast.makeText(this@LoginActivity, "푸시 등록 실패(${state.code})", Toast.LENGTH_SHORT).show()
                            Log.e("FCM", "등록 실패: ${state.code} ${state.msg}")
                        }
                        is DeviceTokenViewModel.UiState.Error -> {
                            Toast.makeText(this@LoginActivity, "네트워크 오류: ${state.msg}", Toast.LENGTH_SHORT).show()
                            Log.e("FCM", "등록 오류: ${state.msg}")
                        }
                    }
                }
            }
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
        
        // FCM 토큰 등록
        fetchAndRegisterFcmToken()
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
        val loginModel = loginViewModel.checkAutoLogin()
        if (loginModel != null) {
            // 자동 로그인 성공 - 바로 메인 화면으로 이동
            navigateToMainActivity(loginModel.userRole)
        }
    }

    private fun Int.dpToPx(context: Context): Int =
        (this * context.resources.displayMetrics.density).toInt()

    //  서버 등록까지 한 번에
    private fun fetchAndRegisterFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "토큰 가져오기 실패", task.exception)
                deviceTokenViewModel.register("") // 빈값 보내지 말고 여기서 종료하는 게 나음
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM", "FCM 토큰: $token")
            deviceTokenViewModel.register(token)
        }
    }
}