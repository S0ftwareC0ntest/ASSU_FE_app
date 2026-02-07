package com.ssu.assu.presentation.user.mypage

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ssu.assu.R
import com.ssu.assu.databinding.FragmentUserMypageAlarmBinding
import com.ssu.assu.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserMypageAlarmFragment : BaseFragment<FragmentUserMypageAlarmBinding>(R.layout.fragment_user_mypage_alarm) {

    private val vm: UserAlarmViewModel by viewModels()
    private var updatingUI = false

    override fun initView() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewLifecycleOwner.lifecycleScope.launch {
                        vm.commit()
                        findNavController().navigateUp()
                    }
                }
            }
        )

        binding.btnAdminAlarmBack.setOnClickListener {
            lifecycleScope.launch {
                vm.commit()
                findNavController().navigateUp()
            }
        }

        binding.switchAdminAlarmPush.setOnCheckedChangeListener { _, checked ->
            if (updatingUI) return@setOnCheckedChangeListener
            vm.onStampClick(checked)
        }

        vm.load()
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.stampEnabled.collect { enabled ->
                    updatingUI = true
                    try {
                        binding.switchAdminAlarmPush.isChecked = enabled
                    } finally {
                        updatingUI = false
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }
}
