package com.ssu.assu.domain.usecase.location

import com.ssu.assu.data.dto.location.ViewportQuery
import com.ssu.assu.data.repository.location.LocationRepository
import com.ssu.assu.domain.model.location.StoreOnMap
import com.ssu.assu.util.RetrofitResult
import jakarta.inject.Inject

class GetNearbyStoresV2UseCase @Inject constructor(
    private val repo: LocationRepository
) {
    suspend operator fun invoke(v: ViewportQuery): RetrofitResult<List<StoreOnMap>> =
        repo.getNearbyStoresV2(v)
}
