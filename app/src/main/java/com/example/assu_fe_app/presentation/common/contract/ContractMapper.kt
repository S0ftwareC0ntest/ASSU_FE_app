package com.example.assu_fe_app.presentation.common.contract

import com.example.assu_fe_app.data.dto.partnership.PartnershipContractData
import com.example.assu_fe_app.data.dto.partner_admin.home.PartnershipContractItem
import com.example.assu_fe_app.domain.model.partnership.PartnershipDetailModel
import com.example.assu_fe_app.domain.model.partnership.PartnershipOptionModel

/**
 * Domain -> Dialog 전용 데이터로 변환
 *
 * @param partnerNameFallback 카드/리스트 상호명(서버 필드 없을 때 사용)
 * @param adminNameFallback   관리자명 임시값(실명 필요 시 교체)
 * @param fallbackStart       서버 기간 누락 시 대체 시작일 (YYYY-MM-DD)
 * @param fallbackEnd         서버 기간 누락 시 대체 종료일 (YYYY-MM-DD)
 */
fun PartnershipDetailModel.toContractData(
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
        adminName = adminNameFallback ?: "관리자",
        options = items,
        periodStart = start,
        periodEnd = end
    )
}

/**
 * PartnershipOptionModel -> PartnershipContractItem
 *
 * optionType:   "SERVICE" | "DISCOUNT"
 * criterionType:"PEOPLE"  | "PRICE"
 */
private fun PartnershipOptionModel.toContractItem(): PartnershipContractItem? {
    val opt = optionType?.uppercase().orEmpty()
    val cri = criterionType?.uppercase().orEmpty()

    // goods 이름을 ", "로 연결 (없으면 빈 문자열)
    val goodsNames = goods.joinToString(", ") { it.name }

    return when (opt) {
        "SERVICE" -> when (cri) {
            "PEOPLE" -> PartnershipContractItem.Service.ByPeople(
                minPeople = (people ?: 0).coerceAtLeast(0),
                items = goodsNames
            )
            "PRICE" -> PartnershipContractItem.Service.ByAmount(
                minAmount = (cost ?: 0).coerceAtLeast(0),
                items = goodsNames
            )
            else -> null
        }
        "DISCOUNT" -> when (cri) {
            "PEOPLE" -> PartnershipContractItem.Discount.ByPeople(
                minPeople = (people ?: 0).coerceAtLeast(0),
                percent = (discountRate ?: 0).coerceIn(0, 100)
            )
            "PRICE" -> PartnershipContractItem.Discount.ByAmount(
                minAmount = (cost ?: 0).coerceAtLeast(0),
                percent = (discountRate ?: 0).coerceIn(0, 100)
            )
            else -> null
        }
        else -> null
    }
}