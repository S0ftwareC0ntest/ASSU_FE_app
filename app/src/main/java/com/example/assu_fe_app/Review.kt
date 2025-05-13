package com.example.assu_fe_app

import java.time.LocalDateTime
import java.util.Date

data class Review(
    var marketName : String,
    var rate : Int,
    var content : String,
    var reviewImage : List<String>,
    var date: LocalDateTime
)
