package com.example.assu_fe_app.presentation.common.location

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.assu_fe_app.R
import com.example.assu_fe_app.data.dto.UserRole
import com.example.assu_fe_app.data.dto.chatting.request.CreateChatRoomRequestDto
import com.example.assu_fe_app.data.dto.location.LocationAdminPartnerSearchResultItem
import com.example.assu_fe_app.data.manager.TokenManager
import com.example.assu_fe_app.databinding.ItemLocationBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.common.contract.PartnershipContractDialogFragment
import com.example.assu_fe_app.ui.chatting.ChattingViewModel
import com.example.assu_fe_app.ui.partnership.PartnershipViewModel
import com.example.assu_fe_app.presentation.common.contract.toContractData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationItemFragment :
    BaseFragment<ItemLocationBinding>(R.layout.item_location) {

    private val chatVm: ChattingViewModel by activityViewModels()
    private val partnershipVm: PartnershipViewModel by activityViewModels()

    @Inject lateinit var tokenManager: TokenManager

    private var lastItem: LocationAdminPartnerSearchResultItem? = null
    private var pendingPartnershipId: Long? = null

    private val role: UserRole by lazy {
        tokenManager.getUserRoleEnum() ?: UserRole.ADMIN
    }

    override fun initView() = Unit

    override fun initObserver() {
        // 제휴 상세 상태 구독: 성공 시 다이얼로그 표시
        viewLifecycleOwner.lifecycleScope.launch {
            partnershipVm.getPartnershipDetailUiState.collect { state ->
                when (state) {
                    is PartnershipViewModel.PartnershipDetailUiState.Loading -> {
                        showLoading(true)
                    }
                    is PartnershipViewModel.PartnershipDetailUiState.Success -> {
                        showLoading(false)
                        val wanted = pendingPartnershipId
                        if (wanted == null || state.data.partnershipId != wanted) return@collect
                        pendingPartnershipId = null

                        val current = lastItem
                        val (fallbackStart, fallbackEnd) = parseTerm(current?.term)

                        val data = state.data.toContractData(
                            partnerNameFallback = current?.shopName ?: "-",
                            adminNameFallback   = tokenManager.getUserName() ?: "관리자",
                            fallbackStart = fallbackStart,
                            fallbackEnd = fallbackEnd
                        )

                        PartnershipContractDialogFragment
                            .newInstance(data)
                            .show(parentFragmentManager, "PartnershipContractDialog")
                    }
                    is PartnershipViewModel.PartnershipDetailUiState.Fail -> {
                        showLoading(false)
                        pendingPartnershipId = null
                        toast(state.message ?: "서버 처리 실패(${state.code})")
                    }
                    is PartnershipViewModel.PartnershipDetailUiState.Error -> {
                        showLoading(false)
                        pendingPartnershipId = null
                        toast(state.message)
                    }
                    PartnershipViewModel.PartnershipDetailUiState.Idle -> Unit
                }
            }
        }
    }

    fun showCapsuleInfo(item: LocationAdminPartnerSearchResultItem) {
        lastItem = item

        binding.tvAdminPartnerLocationShopName.text = item.shopName

        if (item.isPartnered) {
            binding.ivAdminPartnerLocationCapsule.visibility = View.VISIBLE
            binding.tvAdminPartnerLocationCapsuleText.visibility = View.VISIBLE
            binding.tvAdminPartnerLocationAddressDate.text = item.term
            binding.ivAdminPartnerLocationImg.setBackgroundResource(R.drawable.img_partner)
        } else {
            binding.ivAdminPartnerLocationCapsule.visibility = View.GONE
            binding.tvAdminPartnerLocationCapsuleText.visibility = View.GONE
            binding.tvAdminPartnerLocationAddressDate.text = item.address
            binding.ivAdminPartnerLocationImg.setBackgroundResource(R.drawable.img_ssu)
        }

        binding.tvAdminPartnerLocationContact.text =
            if (item.isPartnered) "제휴 계약서 보기" else "문의하기"

        val clicker = View.OnClickListener {
            val current = lastItem ?: return@OnClickListener

            if (!current.isPartnered) {
                // 채팅방 생성
                val req = when (role) {
                    UserRole.ADMIN -> {
                        val adminId   = tokenManager.getUserId()  ?: return@OnClickListener
                        val partnerId = current.id.toLongOrNull() ?: return@OnClickListener
                        CreateChatRoomRequestDto(adminId = adminId, partnerId = partnerId)
                    }
                    UserRole.PARTNER -> {
                        val adminId   = current.id.toLongOrNull() ?: return@OnClickListener
                        val partnerId = tokenManager.getUserId()  ?: return@OnClickListener
                        CreateChatRoomRequestDto(adminId = adminId, partnerId = partnerId)
                    }
                    else -> return@OnClickListener
                }
                chatVm.createRoom(req)
            } else {
                // 제휴 계약서 보기: partnershipId 필요
                val partnershipId: Long? = current.partnershipId
                if (partnershipId == null) {
                    // partnershipId 없으면 카드 정보로 임시 표시
                    openContractDialogFallback(current)
                    return@OnClickListener
                }
                pendingPartnershipId = partnershipId
                partnershipVm.getPartnershipDetail(partnershipId)
            }
        }

        binding.ivAdminPartnerLocationContact.setOnClickListener(clicker)
        binding.tvAdminPartnerLocationContact.setOnClickListener(clicker)
    }

    // partnershipId 없거나 API 실패 시 임시 다이얼로그
    private fun openContractDialogFallback(item: LocationAdminPartnerSearchResultItem) {
        val (start, end) = parseTerm(item.term)
        val data = com.example.assu_fe_app.data.dto.partnership.PartnershipContractData(
            partnerName = item.shopName,
            adminName = "관리자",
            options = emptyList(),
            periodStart = start,
            periodEnd = end
        )

        PartnershipContractDialogFragment
            .newInstance(data)
            .show(parentFragmentManager, "PartnershipContractDialog")
    }

    private fun parseTerm(term: String?): Pair<String?, String?> {
        if (term.isNullOrBlank()) return null to null
        val parts = term.split("~").map { it.trim() }
        return parts.getOrNull(0) to parts.getOrNull(1)
    }

    // 필요 시 프로젝트 공통 유틸과 교체
    private fun toast(msg: String) {
        android.widget.Toast.makeText(requireContext(), msg, android.widget.Toast.LENGTH_SHORT).show()
    }
    private fun showLoading(show: Boolean) {
        // TODO: ProgressBar 노출/숨김 (프로젝트 공통 로딩 뷰 사용 시 교체)
    }
}