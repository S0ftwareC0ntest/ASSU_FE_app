package com.ssu.assu.data.dto.location.response

import com.ssu.assu.domain.model.location.StoreOnMap
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoreMapResponseV2Dto(
    val storeId: Long,
    val name: String,
    val address: String?,
    val rate: Int?,
    val hasPartner: Boolean,
    val latitude: Double,
    val longitude: Double,
    val profileUrl: String?,
    val phoneNumber: String?,
    val partnerships: List<PartnershipInfo>?
) {
    @JsonClass(generateAdapter = true)
    data class PartnershipInfo(
        val adminId: Long,
        val adminName: String,
        val benefit: String
    )

    fun toModel() = StoreOnMap(
        storeId = this.storeId,
        adminId = partnerships?.getOrNull(0)?.adminId,
        adminName = partnerships?.getOrNull(0)?.adminName ?: "",
        name = this.name,
        address = this.address,
        rate = this.rate?.toDouble(),
        criterionType = null,
        optionType = null,
        people = null,
        cost = null,
        category = null,
        discountRate = null,
        hasPartner = this.hasPartner,
        latitude = this.latitude,
        longitude = this.longitude,
        profileUrl = this.profileUrl,
        phoneNumber = this.phoneNumber,
        partnerships = partnerships?.map { 
            StoreOnMap.Partnership(
                adminId = it.adminId,
                adminName = it.adminName,
                benefit = it.benefit
            )
        }
    )
}
