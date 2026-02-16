package com.ssu.assu.data.dto.store

data class StorePartnershipResponseDto(
    val partnershipContents: List<PaperContent>,
    val storeId: Long,
    val storeName: String
)