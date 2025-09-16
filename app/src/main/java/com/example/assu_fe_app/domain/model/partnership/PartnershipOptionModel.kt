package com.example.assu_fe_app.domain.model.partnership

data class PartnershipOptionModel(
    val optionType: String,
    val criterionType: String,
    val people: Int,
    val cost: Int,
    val category: String,
    val discountRate: Int,
    val goods: List<GoodsModel>
)