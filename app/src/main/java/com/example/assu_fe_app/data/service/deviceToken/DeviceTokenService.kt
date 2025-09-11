package com.example.assu_fe_app.data.service.deviceToken

import com.example.assu_fe_app.data.dto.auth.BaseResponseDto
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface DeviceTokenService {
    @POST("device-tokens")
    suspend fun registerToken(
        @Query("token") token: String
    ): BaseResponseDto<String>
    
    @DELETE("device-tokens/{tokenId}")
    suspend fun unregisterToken(
        @Path("tokenId") tokenId: Int
    ): BaseResponseDto<Unit>
}