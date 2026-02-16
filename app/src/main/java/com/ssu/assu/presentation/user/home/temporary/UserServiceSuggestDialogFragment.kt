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

    private val viewModel : SuggestionViewModel by activityViewModels()
    private var _binding : FragmentUserServiceSuggestDialogBinding? = null
    private val binding get() = _binding!!

    private var listener: OnServiceSuggestListener? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserServiceSuggestDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnServiceSuggestListener) {
            listener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // X 버튼 → 닫기
        binding.btnServiceSuggestTargetCross.setOnClickListener {
            dismiss()
        }


        // 취소 버튼
        binding.btnServiceSuggestTargetCancel.setOnClickListener {
            dismiss()
        }


        binding.btnServiceSuggestSubmit.setOnClickListener {
            listener?.onServiceSuggest()
            dismiss()
        }
    }

    private fun initView(){
        binding.tvServiceSuggestOrg.text = viewModel.selectedTarget.value?.name
        binding.tvServiceSuggestWant.text = viewModel.storeName.value
        binding.tvServiceSuggestContent.text=viewModel.benefit.value
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

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


}