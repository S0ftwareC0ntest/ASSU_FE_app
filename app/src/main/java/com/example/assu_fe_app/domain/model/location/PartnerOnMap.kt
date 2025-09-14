package com.example.assu_fe_app.domain.model.location

data class PartnerOnMap(
    val partnerId: Long,
    val name: String,
    val address: String?,
    val isPartnered: Boolean,
    val partnershipId: Long?,
    val partnershipStartDate: String?,
    val partnershipEndDate: String?,
    val latitude: Double,
    val longitude: Double
)