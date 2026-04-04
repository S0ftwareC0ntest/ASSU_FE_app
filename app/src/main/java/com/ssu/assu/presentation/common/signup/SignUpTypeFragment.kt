package com.ssu.assu.presentation.common.signup

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssu.assu.R
import com.ssu.assu.databinding.FragmentSignUpTypeBinding
import com.ssu.assu.presentation.base.BaseFragment
import com.ssu.assu.ui.auth.SignUpViewModel
import com.ssu.assu.util.setProgressBarFillAnimated

class SignUpTypeFragment : BaseFragment<FragmentSignUpTypeBinding>(R.layout.fragment_sign_up_type){

    private val signUpViewModel: SignUpViewModel by activityViewModels()
    
    // 선택된 타입을 저장: "admin", "partner", "user"
    private var selectedType: String? = null

    override fun initObserver() {}

    override fun initView() {
        binding.ivSignupProgressBar.setProgressBarFillAnimated(
            container = binding.flSignupProgressContainer,
            fromPercent = 0.1f,
            toPercent = 0.25f
        )
        // 완료 버튼 기본 비활성화
        binding.btnCompleted.isEnabled = false

        binding.btnUserType.setOnClickListener {
            selectType("user")
        }

        // 확인 버튼 클릭 시 (학생 가입만 지원)
        binding.btnCompleted.setOnClickListener {
            if (selectedType == "user") {
                signUpViewModel.setUserType("user")
                findNavController().navigate(R.id.action_type_to_user_school)
            }
        }

        selectType("user")
    }

    private fun selectType(type: String) {
        selectedType = type

        binding.btnAdminType.setBackgroundResource(R.drawable.bg_signup_input_bar)
        binding.btnPartnerType.setBackgroundResource(R.drawable.bg_signup_input_bar)
        binding.btnUserType.setBackgroundResource(R.drawable.bg_signup_input_bar)

        binding.flAdminType.alpha = 0.35f
        binding.flPartnerType.alpha = 0.35f
        binding.flUserType.alpha = 0.6f

        if (type == "user") {
            binding.btnUserType.setBackgroundResource(R.drawable.bg_signup_input_bar_selected)
            binding.flUserType.alpha = 1.0f
        }

        binding.btnCompleted.isEnabled = true
        binding.btnCompleted.setBackgroundResource(R.drawable.btn_basic_selected)
    }
}