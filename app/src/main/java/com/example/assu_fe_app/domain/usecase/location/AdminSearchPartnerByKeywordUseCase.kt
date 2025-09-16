package com.example.assu_fe_app.domain.usecase.location

import com.example.assu_fe_app.data.service.location.LocationService
import javax.inject.Inject

class AdminSearchPartnerByKeywordUseCase @Inject constructor(
    private val locationService: LocationService
) {
    suspend operator fun invoke(keyword: String)
    = locationService.searchPartners(keyword)

}