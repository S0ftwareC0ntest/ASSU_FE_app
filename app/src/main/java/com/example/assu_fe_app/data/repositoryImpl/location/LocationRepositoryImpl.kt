package com.example.assu_fe_app.data.repositoryImpl.location

import com.example.assu_fe_app.data.dto.location.ViewportQuery
import com.example.assu_fe_app.data.repository.location.LocationRepository
import com.example.assu_fe_app.data.service.location.LocationService
import com.example.assu_fe_app.domain.model.location.AdminOnMap
import com.example.assu_fe_app.domain.model.location.PartnerOnMap
import com.example.assu_fe_app.domain.model.location.StoreOnMap
import com.example.assu_fe_app.util.RetrofitResult
import com.example.assu_fe_app.util.apiHandler
import jakarta.inject.Inject


class LocationRepositoryImpl @Inject constructor(
    private val api: LocationService
) : LocationRepository {

    override suspend fun getNearbyPartners(v: ViewportQuery): RetrofitResult<List<PartnerOnMap>> =
        apiHandler(
            execute = { api.getPartners(v.lng1, v.lat1, v.lng2, v.lat2, v.lng3, v.lat3, v.lng4, v.lat4) },
            mapper  = { list -> list.map { it.toModel() } }
        )

    override suspend fun getNearbyAdmins(v: ViewportQuery): RetrofitResult<List<AdminOnMap>> =
        apiHandler(
            execute = { api.getAdmins(v.lng1, v.lat1, v.lng2, v.lat2, v.lng3, v.lat3, v.lng4, v.lat4) },
            mapper  = { list -> list.map { it.toModel() } }
        )

    override suspend fun getNearbyStores(v: ViewportQuery): RetrofitResult<List<StoreOnMap>> =
        apiHandler(
            execute = { api.getStores(v.lng1, v.lat1, v.lng2, v.lat2, v.lng3, v.lat3, v.lng4, v.lat4) },
            mapper  = { list -> list.map { it.toModel() } }
        )
}
