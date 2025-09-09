package com.example.assu_fe_app.data.repository.notification

import com.example.assu_fe_app.domain.model.notification.NotificationSettingsModel
import com.example.assu_fe_app.domain.model.notification.NotificationTypeModel
import com.example.assu_fe_app.util.RetrofitResult

interface NotificationRepository {
    suspend fun toggle(type: NotificationTypeModel): RetrofitResult<NotificationSettingsModel>
    suspend fun getSettings(): RetrofitResult<NotificationSettingsModel>
}