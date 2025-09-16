package com.example.assu_fe_app.data.dto.partnership.response

import com.example.assu_fe_app.domain.model.partnership.PartnershipOptionModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PartnershipOptionResponseDto(
    val optionType: String?,     // enum이면 String 유지
    val criterionType: String?,  // enum이면 String 유지
    val people: Int?,
    val cost: Int?,
    val category: String?,
    val discountRate: Int?,      // 서버의 discount와 매핑
    val goods: List<GoodsResponseDto>?
) {
    fun toModel() = PartnershipOptionModel(
        optionType = optionType.orEmpty(),
        criterionType = criterionType.orEmpty(),
        people = people ?: 0,
        cost = cost ?: 0,
        category = category.orEmpty(),
        discountRate = discountRate ?: 0,
        goods = (goods ?: emptyList()).map { it.toModel() }
    )
}