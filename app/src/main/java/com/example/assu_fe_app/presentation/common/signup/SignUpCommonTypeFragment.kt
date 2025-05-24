package com.example.assu_fe_app.presentation.common.signup

import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentSignUpCommonTypeBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import androidx.navigation.fragment.findNavController

class SignUpCommonTypeFragment : BaseFragment<FragmentSignUpCommonTypeBinding>(R.layout.fragment_sign_up_common_type){

    // 선택된 타입을 저장: "admin", "partner", "user"
    private var selectedType: String? = null

    override fun initObserver() {}

    override fun initView() {
        // 완료 버튼 기본 비활성화
        binding.btnCompleted.isEnabled = false

        // 3개 버튼에 클릭 리스너 설정
        binding.btnAdminType.setOnClickListener {
            selectType("admin")
        }

        binding.btnPartnerType.setOnClickListener {
            selectType("partner")
        }

        binding.btnUserType.setOnClickListener {
            selectType("user")
        }

        // 확인 버튼 클릭 시
        binding.btnCompleted.setOnClickListener {
            when (selectedType) {
                "admin" -> {
                    findNavController().navigate(R.id.action_type_to_admin_info)
                }
                "partner" -> {
                    findNavController().navigate(R.id.action_type_to_partner_info)
                }
                "user" -> {
                    findNavController().navigate(R.id.action_type_to_user_school)
                }
            }
        }
    }

    private fun selectType(type: String) {
        selectedType = type

        // 모든 버튼 기본 배경으로 초기화
        binding.btnAdminType.setBackgroundResource(R.drawable.bg_signup_input_bar)
        binding.btnPartnerType.setBackgroundResource(R.drawable.bg_signup_input_bar)
        binding.btnUserType.setBackgroundResource(R.drawable.bg_signup_input_bar)

        binding.flAdminType.alpha = 0.6f
        binding.flPartnerType.alpha = 0.6f
        binding.flUserType.alpha = 0.6f

        // 선택된 버튼만 강조 및 투명도 설정
        when (type) {
            "admin" -> {
                binding.btnAdminType.setBackgroundResource(R.drawable.bg_signup_input_bar_selected)
                binding.flAdminType.alpha = 1.0f
            }
            "partner" -> {
                binding.btnPartnerType.setBackgroundResource(R.drawable.bg_signup_input_bar_selected)
                binding.flPartnerType.alpha = 1.0f
            }
            "user" -> {
                binding.btnUserType.setBackgroundResource(R.drawable.bg_signup_input_bar_selected)
                binding.flUserType.alpha = 1.0f
            }
        }

        // 완료 버튼 활성화
        binding.btnCompleted.isEnabled = true
        binding.btnCompleted.setBackgroundResource(R.drawable.btn_basic_selected)
    }
}