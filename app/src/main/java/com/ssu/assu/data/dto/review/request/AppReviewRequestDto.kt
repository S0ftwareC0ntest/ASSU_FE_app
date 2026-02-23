package com.ssu.assu.data.dto.review.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppReviewRequestDto(
    val rate: Int,
    val content: String
)
