package com.example.assu_fe_app.data.dto.servicerecord

import java.time.LocalDateTime

data class ServiceRecord(
    var marketName: String,
    var serviceContent: String,
    var dateTime : LocalDateTime,
    var isReviewd : Boolean
)