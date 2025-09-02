package com.example.assu_fe_app.data.service.deviceToken

import com.example.assu_fe_app.data.dto.BaseResponse
import com.example.assu_fe_app.data.dto.deviceToken.request.DeviceTokenRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path


interface DeviceTokenService {
    @POST("deviceTokens/register")
    suspend fun registerToken(
        @Body body: DeviceTokenRequestDto
    ): BaseResponse<Long>

    @DELETE("deviceTokens/unregister/{tokenId}")
    suspend fun unregisterToken(
        @Path("tokenId") tokenId: Long
    ): BaseResponse<String>
}