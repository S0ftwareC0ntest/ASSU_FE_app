package com.example.assu_fe_app.data.dto.auth

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponseDto<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T?
)
