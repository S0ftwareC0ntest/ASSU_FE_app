package com.example.assu_fe_app.data.service.deviceToken

import com.example.assu_fe_app.data.dto.auth.BaseResponseDto
import com.example.assu_fe_app.data.dto.deviceToken.request.DeviceTokenRequestDto
import retrofit2.http.Body
import retrofit2.http.POST


interface DeviceTokenService {
    @POST("deviceTokens/register")
    suspend fun registerToken(
        @Body body: DeviceTokenRequestDto
    ): BaseResponseDto<String> // 서버 응답: BaseResponseDto<String>
}