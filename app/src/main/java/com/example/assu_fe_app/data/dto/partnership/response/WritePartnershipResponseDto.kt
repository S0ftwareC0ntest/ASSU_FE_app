package com.example.assu_fe_app.data.dto.partnership.response

import com.example.assu_fe_app.domain.model.partnership.PartnershipDetailModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WritePartnershipResponseDto(
    val partnershipId: Long,
    val partnershipPeriodStart: String?,   // ISO-8601 문자열이라고 가정
    val partnershipPeriodEnd: String?,
    val adminId: Long?,                    // 수동등록이면 null 가능
    val partnerId: Long?,                  // 수동등록이면 null
    val storeId: Long?,
    val options: List<PartnershipOptionResponseDto>?
) {
    fun toModel() = PartnershipDetailModel(
        partnershipId = partnershipId,
        periodStart = partnershipPeriodStart.orEmpty(),
        periodEnd = partnershipPeriodEnd.orEmpty(),
        adminId = adminId,
        partnerId = partnerId,
        storeId = storeId,
        options = (options ?: emptyList()).map { it.toModel() }
    )
}