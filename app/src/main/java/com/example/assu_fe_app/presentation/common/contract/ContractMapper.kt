package com.example.assu_fe_app.presentation.common.contract

import android.util.Log
import com.example.assu_fe_app.data.dto.partnership.PartnershipContractData
import com.example.assu_fe_app.data.dto.partner_admin.home.PartnershipContractItem
import com.example.assu_fe_app.data.dto.partnership.response.OptionType
import com.example.assu_fe_app.data.dto.partnership.response.CriterionType
import com.example.assu_fe_app.domain.model.partnership.PartnershipOptionModel
import com.example.assu_fe_app.domain.model.partnership.ProposalPartnerDetailsModel

fun ProposalPartnerDetailsModel.toContractData(
    partnerNameFallback: String,
    adminNameFallback: String? = "관리자",
    fallbackStart: String? = null,
    fallbackEnd: String? = null
): PartnershipContractData {

    val items: List<PartnershipContractItem> =
        options.mapNotNull { it.toContractItem() }

    val start = periodStart.takeUnless { it.isNullOrBlank() } ?: (fallbackStart ?: "")
    val end   = periodEnd  .takeUnless { it.isNullOrBlank() } ?: (fallbackEnd ?: "")

    return PartnershipContractData(
        partnerName = partnerNameFallback,
        adminName   = adminNameFallback ?: "관리자",
        options     = items,
        periodStart = start,
        periodEnd   = end
    )
}

/**
 * PartnershipOptionModel (domain.model.admin) -> PartnershipContractItem
 */
private fun PartnershipOptionModel.toContractItem(): PartnershipContractItem? {
    Log.d(
        "OptionDebug",
        "optionType=$optionType, criterionType=$criterionType, people=$people, cost=$cost, " +
                "category=$category, discountRate=$discountRate, goods=${goods.map { it.goodsName }}"
    )

    // goods 우선, 없으면 category 사용
    val goodsNames = when {
        goods.isNotEmpty()     -> goods.joinToString(", ") { it.goodsName }
        category.isNotBlank()  -> category
        else                   -> ""
    }

    return when (optionType) {
        OptionType.SERVICE -> when (criterionType) {
            CriterionType.HEADCOUNT -> PartnershipContractItem.Service.ByPeople(
                minPeople = people.coerceAtLeast(0),
                items     = goodsNames
            )
            CriterionType.PRICE -> PartnershipContractItem.Service.ByAmount(
                // ✅ Long -> Int 변환
                minAmount = cost.toInt().coerceAtLeast(0),
                items     = goodsNames
            )
        }
        OptionType.DISCOUNT -> when (criterionType) {
            CriterionType.HEADCOUNT -> PartnershipContractItem.Discount.ByPeople(
                minPeople = people.coerceAtLeast(0),
                percent   = discountRate.toInt().coerceIn(0, 100)
            )
            CriterionType.PRICE -> PartnershipContractItem.Discount.ByAmount(
                // ✅ Long -> Int 변환
                minAmount = cost.toInt().coerceAtLeast(0),
                percent   = discountRate.toInt().coerceIn(0, 100)
            )
        }
    }
}