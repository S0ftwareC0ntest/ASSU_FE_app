package com.example.assu_fe_app.presentation.common.location

import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.assu_fe_app.R
import com.example.assu_fe_app.data.dto.UserRole
import com.example.assu_fe_app.data.dto.chatting.request.CreateChatRoomRequestDto
import com.example.assu_fe_app.data.dto.location.LocationAdminPartnerSearchResultItem
import com.example.assu_fe_app.data.manager.TokenManager
import com.example.assu_fe_app.databinding.ItemLocationBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.ui.chatting.ChattingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// LocationItemFragment.kt
@AndroidEntryPoint
class LocationItemFragment :
    BaseFragment<ItemLocationBinding>(R.layout.item_location) {

    private val chatVm: ChattingViewModel by activityViewModels()

    @Inject lateinit var tokenManager: TokenManager

    private var lastItem: LocationAdminPartnerSearchResultItem? = null
    private val role: UserRole by lazy {
        tokenManager.getUserRoleEnum() ?: UserRole.ADMIN
    }

    override fun initObserver() = Unit
    override fun initView() = Unit

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
                val req = when (role) {
                    UserRole.ADMIN -> {
                        // ADMIN: adminId = 내 ID, partnerId = 상대(파트너) ID
                        val adminId   = tokenManager.getUserId()            ?: return@OnClickListener
                        val partnerId = current.id.toLongOrNull()           ?: return@OnClickListener
                        CreateChatRoomRequestDto(adminId = adminId, partnerId = partnerId)
                    }
                    UserRole.PARTNER -> {
                        // PARTNER: adminId = 상대(관리자) ID, partnerId = 내 ID
                        val adminId   = current.id.toLongOrNull()           ?: return@OnClickListener
                        val partnerId = tokenManager.getUserId()            ?: return@OnClickListener
                        CreateChatRoomRequestDto(adminId = adminId, partnerId = partnerId)
                    }
                    else -> return@OnClickListener
                }
                chatVm.createRoom(req)
            } else {
                // 제휴 계약서 보기
                openContractDialog(current)
            }
        }

        binding.ivAdminPartnerLocationContact.setOnClickListener(clicker)
        binding.tvAdminPartnerLocationContact.setOnClickListener(clicker)
    }

    /**
     * 제휴 계약서 다이얼로그 오픈
     * - 현재 캡슐의 정보로 기본 ContractData를 만들어 다이얼로그에 전달
     * - 추후 API 연동 시, 여기서 비동기 호출로 실제 데이터를 받아서 넘기면 됨
     */
    private fun openContractDialog(item: LocationAdminPartnerSearchResultItem) {
        // term: "YYYY-MM-DD ~ YYYY-MM-DD" 형태 가정
        val (start, end) = parseTerm(item.term)

        val data = com.example.assu_fe_app.data.dto.partnership.PartnershipContractData(
            partnerName = item.shopName,              // 파트너명: 현재 카드 상호명으로 대체
            adminName = "관리자",                       // 필요 시 서버데이터로 교체
            periodStart = start,
            periodEnd = end,
            options = emptyList()                     // 옵션은 API 연동 뒤 실제 값으로 대체
        )

        val dialog = com.example.assu_fe_app.presentation.common.contract
            .PartnershipContractDialogFragment
            .newInstance(data)

        // LocationItemFragment는 child로 붙어 있으므로 activity 혹은 parentFragmentManager 사용
        dialog.show(parentFragmentManager, "PartnershipContractDialog")
    }

    private fun parseTerm(term: String?): Pair<String?, String?> {
        if (term.isNullOrBlank()) return null to null
        // "2025-09-14 ~ 2025-11-14" 형태 분해
        return term.split("~")
            .map { it.trim() }
            .let { parts ->
                val start = parts.getOrNull(0)
                val end = parts.getOrNull(1)
                start to end
            }
    }
}