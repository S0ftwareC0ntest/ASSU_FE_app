package com.example.assu_fe_app.data.dto.location.response

import com.example.assu_fe_app.domain.model.location.PartnerOnMap
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PartnerMapResponseDto(
    val partnerId: Long,
    val name: String,
    val address: String?,
    val isPartnered: Boolean,
    val partnershipId: Long?,
    val partnershipStartDate: String?,
    val partnershipEndDate: String?,
    val latitude: Double,
    val longitude: Double
) {
    fun toModel() = PartnerOnMap(
        partnerId = this.partnerId,
        name = this.name,
        address = this.address,
        isPartnered = this.isPartnered,
        partnershipId = this.partnershipId,
        partnershipStartDate = this.partnershipStartDate,
        partnershipEndDate = this.partnershipEndDate,
        latitude = this.latitude,
        longitude = this.longitude
    )
}