package com.ssu.assu.data.dto.location.response

import com.ssu.assu.domain.model.location.StoreOnMap
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoreMapResponseDto(
    val storeId: Long? = null,
    // 아래 필드들은 JSON 루트에 없으므로 ?를 붙이고 기본값 null 설정
    val adminId: Long? = null,
    val adminName: String? = null,
    val criterionType: String? = null,
    val optionType: String? = null,
    val people: Int? = null,
    val cost: Int? = null,
    val category: String? = null,
    val discountRate: Int? = null,

    val name: String,
    val address: String?,
    val rate: Double?,
    val hasPartner: Boolean,
    val latitude: Double,
    val longitude: Double,
    val profileUrl: String? = null,
    val phoneNumber: String? = null,
    val partnerships: List<PartnershipDto>? = null,
) {
    // PartnershipDto 내부는 지금처럼 유지 (여기에 adminName이 있는 건 맞음)
    @JsonClass(generateAdapter = true)
    data class PartnershipDto(
        val adminId: Long,
        val adminName: String,
        val benefits: List<String>
    )

    fun toModel() = StoreOnMap(
        storeId = this.storeId,
        adminId = this.adminId,
        adminName = this.adminName ?: this.partnerships?.firstOrNull()?.adminName ?: "",        name = this.name,
        address = this.address,
        rate = this.rate,
        criterionType = this.criterionType,
        optionType = this.optionType,
        people = this.people,
        cost = this.cost,
        category = this.category,
        discountRate = this.discountRate,
        hasPartner = this.hasPartner,
        latitude = this.latitude,
        longitude = this.longitude,
        profileUrl = this.profileUrl,
        phoneNumber = this.phoneNumber,
        partnerships = this.partnerships?.map {
            StoreOnMap.Partnership(
                adminId = it.adminId,
                adminName = it.adminName,
                benefits = it.benefits
            )
        }
    )
}