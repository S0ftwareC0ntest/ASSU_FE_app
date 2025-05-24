package com.example.assu_fe_app.data.dto.review

import java.time.LocalDateTime

data class Review(
    var marketName : String,
    var studentCategory : String,
    var rate : Int,
    var content : String,
    var reviewImage : List<String>,
    var date: LocalDateTime
)