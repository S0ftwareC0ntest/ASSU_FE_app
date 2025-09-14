package com.example.assu_fe_app.presentation.common.location

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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

        binding.tvAdminPartnerLocationAddressDate.text =
            if (item.isPartnered) item.shopName else item.address

        if (item.isPartnered) {
            binding.ivAdminPartnerLocationCapsule.visibility = View.VISIBLE
            binding.tvAdminPartnerLocationCapsuleText.visibility = View.VISIBLE
            binding.tvAdminPartnerLocationCapsuleText.text = item.term
        } else {
            binding.ivAdminPartnerLocationCapsule.visibility = View.GONE
            binding.tvAdminPartnerLocationCapsuleText.visibility = View.GONE
        }

        binding.tvAdminPartnerLocationContact.text =
            if (item.isPartnered) "제휴 계약서 보기" else "문의하기"

        val clicker = View.OnClickListener {
            val current = lastItem ?: return@OnClickListener
            if (!current.isPartnered) {
                val myId = tokenManager.getUserId()  // 내 ID
                val otherId = current.id.toLong() // 상대방 ID

                val req = if (role == UserRole.ADMIN) {
                    // 내가 관리자면 → adminId = 내 ID, partnerId = 상대
                    CreateChatRoomRequestDto(adminId = myId, partnerId = otherId)
                } else {
                    // 내가 파트너면 → partnerId = 내 ID, adminId = 상대
                    CreateChatRoomRequestDto(adminId = otherId, partnerId = myId)
                }

                chatVm.createRoom(req)
            } else {
                // TODO: 제휴 계약서 보기 동작 연결
            }
        }

        binding.ivAdminPartnerLocationContact.setOnClickListener(clicker)
        binding.tvAdminPartnerLocationContact.setOnClickListener(clicker)
    }
}