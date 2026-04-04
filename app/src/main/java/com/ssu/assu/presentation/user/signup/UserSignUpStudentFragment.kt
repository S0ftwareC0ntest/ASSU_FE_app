package com.ssu.assu.presentation.user.signup

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.ssu.assu.R
import com.ssu.assu.databinding.FragmentUserSignUpStudentBinding
import com.ssu.assu.presentation.base.BaseFragment
import com.ssu.assu.presentation.common.login.LoginActivity
import com.ssu.assu.ui.auth.SignUpViewModel
import com.ssu.assu.util.setProgressBarFillAnimated
import kotlinx.coroutines.launch
import android.widget.Toast

class UserSignUpStudentFragment :
    BaseFragment<FragmentUserSignUpStudentBinding>(R.layout.fragment_user_sign_up_student) {

    private val signUpViewModel: SignUpViewModel by activityViewModels()

    /** LMS 웹 성공 직후 서버 토큰 검증 API 대기 중이면 true (이때는 버튼 화면으로 복귀하지 않음) */
    private var awaitingStudentVerifyAfterLms = false

    override fun initObserver() {
        // 학생 토큰 검증 결과 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                signUpViewModel.studentVerifyResult.collect { result ->
                    result ?: return@collect
                    if (!isAdded) return@collect
                    awaitingStudentVerifyAfterLms = false
                    binding.flLmsVerifyLoading.visibility = View.GONE
                    // 검증 결과는 확인 화면에서 표시·소비하므로 여기서 clear 하지 않음
                    findNavController().navigate(R.id.action_user_student_to_student_check)
                }
            }
        }

        // 에러 메시지 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                signUpViewModel.errorMessage.collect { error ->
                    error?.let {
                        if (awaitingStudentVerifyAfterLms) {
                            awaitingStudentVerifyAfterLms = false
                            binding.flLmsVerifyLoading.visibility = View.GONE
                            binding.webviewLmsAuth.visibility = View.GONE
                            binding.llDefaultContent.visibility = View.VISIBLE
                        }
                        val exitToLogin = signUpViewModel.consumeExitToLoginAfterError()
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                        if (exitToLogin) navigateToLogin()
                        signUpViewModel.clearError()
                    }
                }
            }
        }
    }

    override fun initView() {
        binding.flLmsVerifyLoading.visibility = View.GONE
        awaitingStudentVerifyAfterLms = false

        binding.ivSignupProgressBar.setProgressBarFillAnimated(
            container = binding.flSignupProgressContainer,
            fromPercent = 0.55f,
            toPercent = 0.70f
        )

        val schoolName = "숭실대학교" // 이후 동적으로 변경 가능
        val baseText = getString(R.string.school_account_text, schoolName)

        // 텍스트에 "숭실대학교" 부분을 assu_main 컬러로 설정
        binding.tvSchoolAccountTitle.text = buildSpannedString {
            append(" ")
            color(ContextCompat.getColor(requireContext(), R.color.assu_main)) {
                append(schoolName)
            }
            append(" 학생이시군요!\n재학중이신 학교를\n인증해주세요!")
        }

        // WebView 설정
        setupWebView()

        // LMS 인증하기 버튼 클릭
        binding.btnLmsAuth.setOnClickListener {
            showLmsAuthWebView()
        }
    }

    override fun onDestroyView() {
        awaitingStudentVerifyAfterLms = false
        super.onDestroyView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webviewLmsAuth.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.assu_background)
        )
        binding.webviewLmsAuth.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            allowFileAccess = true
            allowContentAccess = true
        }

        CookieManager.getInstance().setAcceptCookie(true)

        binding.webviewLmsAuth.webViewClient = object : WebViewClient() {

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return handleLmsUrlLoading(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    return handleLmsUrlLoading(view, request?.url?.toString())
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                val currentUrl = url ?: return

                if (currentUrl.startsWith("https://saint.ssu.ac.kr/webSSO/sso.jsp")) {
                    try {
                        val queryString = currentUrl.split("?")[1]
                        val sToken = queryString.split("sToken=")[1].split("&")[0]
                        val sIdno = queryString.split("sIdno=")[1]
                        handleLmsAuthSuccess(sToken, sIdno)
                    } catch (e: Exception) {
                        handleLmsAuthSuccess()
                    }
                    return
                }
                if (currentUrl.contains("saint.ssu.ac.kr") &&
                    (currentUrl.contains("main") || currentUrl.contains("index") ||
                        currentUrl.contains("dashboard") || currentUrl.contains("portal"))
                ) {
                    handleLmsAuthSuccess()
                }
            }
        }
    }

    private fun handleLmsUrlLoading(view: WebView?, url: String?): Boolean {
        url ?: return false
        if (url.startsWith("https://saint.ssu.ac.kr/webSSO/sso.jsp")) {
            try {
                val queryString = url.split("?")[1]
                val sToken = queryString.split("sToken=")[1].split("&")[0]
                val sIdno = queryString.split("sIdno=")[1]
                handleLmsAuthSuccess(sToken, sIdno)
            } catch (e: Exception) {
                handleLmsAuthSuccess()
            }
            return true
        }
        if (url.contains("saint.ssu.ac.kr") &&
            (url.contains("main") || url.contains("index") || url.contains("dashboard") || url.contains("portal"))
        ) {
            handleLmsAuthSuccess()
            return true
        }
        view?.loadUrl(url)
        return true
    }

    private fun showLmsAuthWebView() {
        awaitingStudentVerifyAfterLms = false
        signUpViewModel.clearStudentVerifyResult()
        binding.flLmsVerifyLoading.visibility = View.GONE
        // 기본 컨텐츠 숨기고 WebView 표시
        binding.llDefaultContent.visibility = View.GONE
        binding.webviewLmsAuth.visibility = View.VISIBLE

        // LMS 로그인 페이지 로드
        binding.webviewLmsAuth.loadUrl("https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fsaint.ssu.ac.kr%2FwebSSO%2Fsso.jsp")
    }

    private fun handleLmsAuthSuccess(sToken: String? = null, sIdno: String? = null) {
        if (sToken != null && sIdno != null) {
            android.util.Log.d("UserSignUpStudentFragment", "Token: $sToken, ID: $sIdno")
            awaitingStudentVerifyAfterLms = true
            binding.flLmsVerifyLoading.visibility = View.VISIBLE
            binding.webviewLmsAuth.visibility = View.VISIBLE
            binding.llDefaultContent.visibility = View.GONE
            signUpViewModel.setStudentToken(sToken, sIdno)
            signUpViewModel.verifyStudentToken()
        } else {
            awaitingStudentVerifyAfterLms = false
            binding.flLmsVerifyLoading.visibility = View.GONE
            Toast.makeText(requireContext(), "인증 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            binding.webviewLmsAuth.visibility = View.GONE
            binding.llDefaultContent.visibility = View.VISIBLE
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    // 유세인트 인증 로직 추가 예정
    private fun validateStudentAccount(id: String, pw: String): Boolean {
        // 임시 로직: "20211234" "1234"일 경우만 통과
        return id == "20211234" && pw == "1234"
    }
}
