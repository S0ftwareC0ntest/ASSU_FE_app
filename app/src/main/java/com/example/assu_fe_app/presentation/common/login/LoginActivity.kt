package com.example.assu_fe_app.presentation.common.login

import android.content.Context
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivityLoginBinding
import com.example.assu_fe_app.presentation.base.BaseActivity
import android.content.Intent
import com.example.assu_fe_app.presentation.common.signup.SignUpActivity
import com.example.assu_fe_app.presentation.user.UserMainActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
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

        // 로그인 클릭 시
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, UserMainActivity::class.java)
            startActivity(intent)
        }

        // 회원가입하기 클릭 시 회원가입 이동
        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initObserver() {
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }


}