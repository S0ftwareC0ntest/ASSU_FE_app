package com.example.assu_fe_app.presentation.user.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.assu_fe_app.R
import com.example.assu_fe_app.data.local.AuthTokenLocalStore
import com.example.assu_fe_app.databinding.FragmentUserMypageBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.common.login.LoginActivity
import com.example.assu_fe_app.ui.common.mypage.MypageViewModel
import com.example.assu_fe_app.presentation.user.review.mypage.UserMyReviewActivity
import com.example.assu_fe_app.ui.profileImage.ProfileImageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserMypageFragment
    : BaseFragment<FragmentUserMypageBinding>(R.layout.fragment_user_mypage) {

    @Inject
    lateinit var authTokenLocalStore: AuthTokenLocalStore

    private val viewModel: MypageViewModel by viewModels()
    private val profileViewModel: ProfileImageViewModel by viewModels()


    override fun initView() { /* no-op */ }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.logoutState.collectLatest { state ->
                when (state) {
                    is MypageViewModel.LogoutState.Done -> navigateToLoginAndClear()
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            profileViewModel.profileUi.collectLatest { s ->
                // 1) 서버에서 받은 presigned URL 있으면 표시
                s.remoteUrl?.let { url ->
                    Glide.with(this@UserMypageFragment)
                        .load(url)
                        .placeholder(R.drawable.img_user) // 선택
                        .error(R.drawable.img_user)        // 선택
                        .into(binding.ivAccountProfileImg)
                }

                // 2) 방금 업로드한 로컬 미리보기 우선 표시 (있으면)
                s.lastLocalPreview?.let { uri ->
                    Glide.with(this@UserMypageFragment)
                        .load(uri)
                        .into(binding.ivAccountProfileImg)
                }

                // 메시지는 필요 시 Snackbar/Toast
                s.message?.let { msg ->
                    // Log.e("Profile", msg) // 또는 Snackbar/Toast
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvAccountName.setText(authTokenLocalStore.getUserName())
        profileViewModel.fetchProfileImage()
        initClick()
    }

    private fun initClick() {
        binding.clAccountComponent1.setOnClickListener {
            startActivity(Intent(requireContext(), UserMyReviewActivity::class.java))
        }

        // 프로필 수정
        binding.clAccountComponent2.setOnClickListener {
            // TODO: 구현 예정
        }

        // 계정관리 페이지 이동
        binding.clAccountComponent3.setOnClickListener {
            findNavController().navigate(
                R.id.action_user_mypage_to_mypage_account
            )
        }

        // 개인정보 처리방침
        binding.clAccountComponent4.setOnClickListener {
            UserMypagePrivacyDialogFragment()
                .show(childFragmentManager, "PrivacyDialog")
        }

        // FAQ
        binding.clAccountComponent5.setOnClickListener {
            UserMypageFAQDialogFragment()
                .show(childFragmentManager, "FAQDialog")
        }

        // 고객센터
        binding.clAccountComponent6.setOnClickListener {
            findNavController().navigate(
                R.id.action_user_mypage_to_inquiry
            )
        }
    }

    private fun navigateToLoginAndClear() {
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}