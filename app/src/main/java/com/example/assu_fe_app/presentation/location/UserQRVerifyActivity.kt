package com.example.assu_fe_app.presentation.location

import android.content.Context
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivityUserQrVerifyBinding
import com.example.assu_fe_app.presentation.base.BaseActivity

class UserQRVerifyActivity :
    BaseActivity<ActivityUserQrVerifyBinding>(R.layout.activity_user_qr_verify) {

    override fun initView() {
        applyWindowInsetPadding()

        // ← 버튼 클릭 시 finish()
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 확인 버튼 클릭 처리
        binding.btnConfirm.setOnClickListener {
            Toast.makeText(this, "인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            // 실제 인증 처리 로직 연결 예정
        }

        // 예시로 학교명, 단과대 표시
        binding.tvUniversity.text = "숭실대학교 학생"
        binding.tvDepartment.text = "[IT대학]"
    }

    override fun initObserver() {
        // 추후 QR 인식 결과 LiveData 등 관찰할 경우
    }

    private fun applyWindowInsetPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val extraPaddingTop = 3 // 추가 padding (dp)
            v.setPadding(
                systemBars.left,
                systemBars.top + extraPaddingTop.dpToPx(v.context),
                systemBars.right,
                0
            )
            insets
        }
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}
