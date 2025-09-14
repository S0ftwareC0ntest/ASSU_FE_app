package com.example.assu_fe_app.data.repository.location

import com.example.assu_fe_app.data.dto.location.ViewportQuery
import com.example.assu_fe_app.domain.model.location.AdminOnMap
import com.example.assu_fe_app.domain.model.location.PartnerOnMap
import com.example.assu_fe_app.domain.model.location.StoreOnMap
import com.example.assu_fe_app.util.RetrofitResult

interface LocationRepository {
    suspend fun getNearbyStores(v: ViewportQuery): RetrofitResult<List<StoreOnMap>>
    suspend fun getNearbyPartners(v: ViewportQuery): RetrofitResult<List<PartnerOnMap>>
    suspend fun getNearbyAdmins(v: ViewportQuery): RetrofitResult<List<AdminOnMap>>
}