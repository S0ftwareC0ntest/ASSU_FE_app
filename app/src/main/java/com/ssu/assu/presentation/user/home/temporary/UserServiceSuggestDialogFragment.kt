package com.ssu.assu.presentation.user.home.temporary

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ssu.assu.databinding.FragmentUserServiceSuggestDialogBinding
import com.ssu.assu.presentation.common.report.OnServiceSuggestListener
import com.ssu.assu.ui.suggestion.SuggestionViewModel

class UserServiceSuggestDialogFragment : DialogFragment() {

    private val viewModel: SuggestionViewModel by activityViewModels()
    private var _binding: FragmentUserServiceSuggestDialogBinding? = null
    private val binding get() = _binding!!

    private var listener: OnServiceSuggestListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = when {
            parentFragment is OnServiceSuggestListener -> parentFragment as OnServiceSuggestListener
            context is OnServiceSuggestListener -> context
            else -> {
                // 필요하다면 예외를 던지거나 로그를 남깁니다.
                null
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserServiceSuggestDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.btnServiceSuggestTargetCross.setOnClickListener { dismiss() }
        binding.btnServiceSuggestTargetCancel.setOnClickListener { dismiss() }

        binding.btnServiceSuggestSubmit.setOnClickListener {
            listener?.onServiceSuggest()
            dismiss()
        }
    }

    private fun initView() {
        // ViewModel 관찰 중이라면 value로 직접 접근하거나
        // 바인딩 시점에서 처리 가능합니다.
        binding.tvServiceSuggestOrg.text = viewModel.selectedTarget.value?.name
        binding.tvServiceSuggestWant.text = viewModel.storeName.value
        binding.tvServiceSuggestContent.text = viewModel.benefit.value
    }

    override fun onResume() {
        super.onResume()
        // 화면 너비 비율 조정
        val displayMetrics = resources.displayMetrics
        val dialogWidth = (displayMetrics.widthPixels * 0.8396f).toInt()
        dialog?.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}