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
                        val storeId = current.storeId                ?: return@OnClickListener
                        val partnerId = tokenManager.getUserId()     ?: return@OnClickListener
                        CreateChatRoomRequestDto(storeId = storeId, partnerId = partnerId)
                    }
                    UserRole.PARTNER -> {
                        val storeId = tokenManager.getUserId()      ?: return@OnClickListener
                        val partnerId = current.id.toLongOrNull()    ?: return@OnClickListener
                        CreateChatRoomRequestDto(storeId = storeId, partnerId = partnerId)
                    }
                    else -> return@OnClickListener
                }
                chatVm.createRoom(req)
            } else {
                // 제휴 계약서 보기 동작
            }
        }

        binding.ivAdminPartnerLocationContact.setOnClickListener(clicker)
        binding.tvAdminPartnerLocationContact.setOnClickListener(clicker)
    }
}