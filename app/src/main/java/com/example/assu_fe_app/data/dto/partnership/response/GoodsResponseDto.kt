package com.example.assu_fe_app.data.dto.partnership.response

import com.example.assu_fe_app.domain.model.partnership.GoodsModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GoodsResponseDto(
    val id: Long?,
    val name: String?,
    val price: Int?,            // 없으면 제거
    val description: String?    // 없으면 제거
) {
    fun toModel() = GoodsModel(
        id = id ?: -1L,
        name = name.orEmpty(),
        price = price ?: 0,
        description = description.orEmpty()
    )
}