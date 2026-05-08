package com.ssu.assu.presentation.user.home

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.ssu.assu.R
import com.ssu.assu.data.dto.certification.request.PersonalCertificationRequestDto
import com.ssu.assu.data.dto.certification.request.UserSessionRequestDto
import com.ssu.assu.databinding.FragmentUserPartnershipSelectBinding
import com.ssu.assu.presentation.base.BaseFragment
import com.ssu.assu.data.dto.store.PaperContent
import com.ssu.assu.presentation.user.home.temporary.UserEventSelectFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserPartnershipSelectFragment :
    BaseFragment<FragmentUserPartnershipSelectBinding>(R.layout.fragment_user_partnership_select) {

    private lateinit var partnershipButtons: List<View>
    private val viewModel: UserVerifyViewModel by activityViewModels()
    private var selectedIndex: Int? = null
    private var contentList: List<PaperContent> = emptyList()
    private var storeId: Long = 0L // TODO : 임시 운영 버전

    override fun initView() {
        // 버튼 리스트 초기화
        partnershipButtons = listOf(
            binding.btnPartnershipSelect1,
            binding.btnPartnershipSelect2,
            binding.btnPartnershipSelect3,
            binding.btnPartnershipSelect4
        )

        // TODO : 임시 운영 버전
        arguments?.let {
            storeId = it.getLong("storeId")
            if (storeId != null) {
                // storeId를 성공적으로 받았는지 로그로 확인
                Log.d("전달된 데이터!!!!!!!", "프래그먼트에서 받은 storeId: $storeId")
            }
        }
        viewModel.storeId = storeId
        viewModel.getStorePartnership()
        // -------------------------------------

        // 선택 완료 버튼 초기 상태
        updateCompleteButtonState(false)


        binding.btnSelectPartnershipComplete.setOnClickListener {
            selectedIndex?.let { index ->
                if (index < contentList.size) {
                    // 선택된 제휴사 정보를 ViewModel에 저장
                    viewModel.selectPartnership(contentList[index])
//                    navigateToComplete() TODO: 운영 버전
                    temporaryNavigateFragment() // TODO : 임시 버전


                }
            }
        }

        binding.clPartnershipSelectToHome.setOnClickListener {
            requireActivity().finish()
        }

//        binding.tvPartnershipSelectMarketName.text = viewModel.storeName.value TODO : 임시버전을 위해 주석 처리


    }

    override fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {

                binding.loadingOverlay.visibility = View.VISIBLE
                binding.clPartnershipSelectFragment.visibility = View.INVISIBLE
                binding.flPartnershipSelectNotExist.visibility = View.GONE // 비어있음 안내도 숨김
            } else {

                binding.loadingOverlay.visibility = View.GONE
                checkEmptyState()
            }
        }

        viewModel.contentList.observe(viewLifecycleOwner) { contents ->
            contentList = contents
            if (!contents.isNullOrEmpty()) {
                bindContentToButtons(contents)
            }
        }

        // TODO : 임시 버전
        viewModel.storeName.observe(viewLifecycleOwner) { storeName ->
            binding.tvPartnershipSelectMarketName.text = storeName
            // UI가 업데이트될 때 로그를 추가하여 확인
            Log.d("프래그먼트 UI 업데이트", "storeName: $storeName")
        }
        // ---------------



    }
    private fun checkEmptyState() {
        if (contentList.isNullOrEmpty()) {
            // 데이터가 없음
            binding.flPartnershipSelectNotExist.visibility = View.VISIBLE
            binding.clPartnershipSelectFragment.visibility = View.GONE
        } else {
            // 데이터가 있음
            binding.flPartnershipSelectNotExist.visibility = View.GONE
            binding.clPartnershipSelectFragment.visibility = View.VISIBLE
        }
    }

    private fun temporaryNavigateFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, UserEventSelectFragment())
            .addToBackStack(null) // 뒤로가기 버튼 유지하려면 추가, 아니면 제거
            .commit()
    }

    private fun bindContentToButtons(contents: List<PaperContent>) {
        // 최대 4개까지만 처리
        val maxButtons = minOf(contents.size, partnershipButtons.size)

        partnershipButtons.forEachIndexed { index, button ->
            if (index < maxButtons) {
                button.visibility = View.VISIBLE

                // 제휴사 데이터를 버튼에 바인딩
                val content = contents[index]
                bindSingleContent(button, content, index)

                // 클릭 리스너 설정
                button.setOnClickListener {
                    updateSelection(index)
                }
            } else {
                // 데이터가 없는 버튼은 숨김
                button.visibility = View.GONE
            }
        }
    }

    private fun bindSingleContent(button: View, content: PaperContent, index: Int) {
        // 각 버튼의 TextView들을 찾아서 데이터 바인딩
        val titleTextView = button.findViewById<TextView>(
            when(index) {
                0 -> R.id.tv_partnership_1_title
                1 -> R.id.tv_partnership_select_2_title
                2 -> R.id.tv_partnership_select_3_title
                3 -> R.id.tv_partnership_select_4_title
                else -> return
            }
        )
        val descTextView = button.findViewById<TextView>(
            when(index) {
                0 -> R.id.tv_partnership_select_1_desc
                1 -> R.id.tv_partnership_select_2_desc
                2 -> R.id.tv_partnership_select_3_desc
                3 -> R.id.tv_partnership_select_4_desc
                else -> return
            }
        )

        // PaperContent의 데이터를 UI에 반영
        titleTextView?.text = content.adminName // 또는 다른 적절한 필드
        descTextView?.text = content.paperContent
    }

    private fun updateSelection(selected: Int) {
        selectedIndex = selected

        partnershipButtons.forEachIndexed { index, layout ->
            val isSelected = (index == selected)

            // 선택 상태에 따른 UI 업데이트
            updateButtonAppearance(layout, index, isSelected)
        }

        // 선택 완료 버튼 활성화
        updateCompleteButtonState(true)
    }

    private fun updateButtonAppearance(layout: View, index: Int, isSelected: Boolean) {
        // 배경 변경
        layout.setBackgroundResource(
            if (isSelected) R.drawable.bg_partnership_selected
            else R.drawable.bg_partnership_unselected
        )

        // 투명도 변경
        layout.alpha = if (isSelected) 1.0f else 0.5f

        // 텍스트 색상 변경
        val titleText = layout.findViewById<TextView>(
            when(index) {
                0 -> R.id.tv_partnership_1_title
                1 -> R.id.tv_partnership_select_2_title
                2 -> R.id.tv_partnership_select_3_title
                3 -> R.id.tv_partnership_select_4_title
                else -> return
            }
        )
        val descText = layout.findViewById<TextView>(
            when(index) {
                0 -> R.id.tv_partnership_select_1_desc
                1 -> R.id.tv_partnership_select_2_desc
                2 -> R.id.tv_partnership_select_3_desc
                3 -> R.id.tv_partnership_select_4_desc
                else -> return
            }
        )

        val color = if (isSelected) R.color.assu_main else R.color.assu_font_main
        titleText?.setTextColor(ContextCompat.getColor(requireContext(), color))
        descText?.setTextColor(ContextCompat.getColor(requireContext(), color))
    }

    private fun updateCompleteButtonState(isEnabled: Boolean) {
        binding.btnSelectPartnershipComplete.apply {
            this.isEnabled = isEnabled
            setBackgroundResource(
                if (isEnabled) R.drawable.btn_basic_selected
                else R.drawable.btn_basic_unselected
            )
        }
    }

    private fun navigateToComplete() {
        Log.d("navigateToComplete 실행", "다음 프래그먼트가 무엇인지 판단 중입니다.")
        if(viewModel.isPeopleType){
            val request = UserSessionRequestDto(
                viewModel.selectedAdminId,
                viewModel.selectedPeople,
                viewModel.storeId,
                viewModel.tableNumber.toInt()
            )

            // 세션 요청 미리 호출
            viewModel.requestSessionId(request)


            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, UserGroupVerifyFragment())
                .addToBackStack(null)
                .commit()
            return
        } else{
            Log.d("제휴 통계 데이터 전송", "인원 수 인증이 아니므로 통계 데이터 및 사용내역을 db에 전송합니다. ")
            // 통계 데이터 용 개인 인증 데이터를 db에 post함.
            viewModel.postPersonalCertification(
                PersonalCertificationRequestDto(
                    viewModel.selectedAdminId,
                    viewModel.storeId,
                    viewModel.tableNumber.toInt()
                )
            )


            if(viewModel.isGoodsList){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, UserSelectServiceFragment())
                    .addToBackStack(null)
                    .commit()
                return
            } else{
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, UserPriceConfirmFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

    }
}