package com.ssu.assu.data.dto.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StudentTokenVerifyResponseDto(
    val studentNumber: String,
    val name: String,
    val enrollmentStatus: String?, // nullable로 변경
    val yearSemester: String,
    @Json(name = "majorStr")
    val major: String
)
