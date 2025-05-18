package com.example.assu_fe_app.presentation.signup

import androidx.navigation.fragment.findNavController
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentSignUpPartnerLicenseBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import androidx.core.content.ContextCompat

class SignUpPartnerLicenseFragment :
    BaseFragment<FragmentSignUpPartnerLicenseBinding>(R.layout.fragment_sign_up_partner_license) {

    override fun initObserver() {}

    override fun initView() {
        // 처음엔 비활성화
        setButtonEnabled(false)

        // 업로드 버튼 클릭
        binding.ivUpload.setOnClickListener {
            val fakeFileName = "LICENSE_${(100..999).random()}"
            binding.etPartnerLicense.setText(fakeFileName)
            binding.ivUpload.setImageResource(R.drawable.ic_signup_verified)
            setButtonEnabled(true)
        }

        binding.btnCompleted.setOnClickListener {
            if (binding.btnCompleted.isEnabled) {
                findNavController().navigate(R.id.action_partner_license_to_complete)
            }
        }
    }

    private fun setButtonEnabled(enabled: Boolean) {
        binding.btnCompleted.isEnabled = enabled
        binding.btnCompleted.background = ContextCompat.getDrawable(
            requireContext(),
            if (enabled) R.drawable.btn_basic_selected else R.drawable.btn_basic_unselected
        )
    }
}