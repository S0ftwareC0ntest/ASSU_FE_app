package com.ssu.assu.presentation.user.home.temporary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.ssu.assu.R
import com.ssu.assu.databinding.DialogAppReviewBinding
import com.ssu.assu.presentation.user.home.UserVerifyViewModel
import com.ssu.assu.util.RetrofitResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppReviewDialogFragment : DialogFragment() {

    private val viewModel: UserVerifyViewModel by activityViewModels()
    private var _binding: DialogAppReviewBinding? = null
    private val binding get() = _binding!!

    private var selectedRate: Int = 0
    private lateinit var starImageViews: List<ImageView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAppReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        starImageViews = listOf(
            binding.ivAppReviewStar1,
            binding.ivAppReviewStar2,
            binding.ivAppReviewStar3,
            binding.ivAppReviewStar4,
            binding.ivAppReviewStar5
        )

        binding.btnAppReviewClose.setOnClickListener { dismiss() }

        starImageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                selectedRate = index + 1
                updateStarAppearance(selectedRate)
            }
        }

        binding.btnAppReviewSubmit.setOnClickListener {
            val content = binding.etAppReviewContent.text?.toString()?.trim().orEmpty()
            if (selectedRate !in 1..5) {
                Toast.makeText(requireContext(), "별점을 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (content.isEmpty()) {
                Toast.makeText(requireContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.submitAppReview(selectedRate, content)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.appReviewSubmitResult.collectLatest { result ->
                when (result) {
                    is RetrofitResult.Success -> dismiss()
                    is RetrofitResult.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "리뷰 작성에 실패했습니다. ${result.exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is RetrofitResult.Fail -> {
                        Toast.makeText(
                            requireContext(),
                            "리뷰 작성에 실패했습니다. ${result.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    null -> { }
                }
            }
        }
    }

    private fun updateStarAppearance(rating: Int) {
        starImageViews.forEachIndexed { index, imageView ->
            val drawableRes = if (index < rating) R.drawable.ic_activated_star
            else R.drawable.ic_deactivated_star
            imageView.setImageResource(drawableRes)
        }
    }

    override fun onResume() {
        super.onResume()
        val displayMetrics = resources.displayMetrics
        val width = displayMetrics.widthPixels
        val dialogWidth = (width * 0.8396f).toInt()
        dialog?.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
