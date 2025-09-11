package com.example.assu_fe_app.data.repository.deviceToken

import com.example.assu_fe_app.util.RetrofitResult

interface DeviceTokenRepository {
    suspend fun register(token: String): RetrofitResult<String>
    suspend fun unregister(tokenId: Int): RetrofitResult<Unit>
}
