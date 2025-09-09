package com.example.assu_fe_app.data.repositoryImpl.notification

import com.example.assu_fe_app.data.repository.notification.NotificationRepository
import com.example.assu_fe_app.data.service.notification.NotificationService
import com.example.assu_fe_app.domain.model.notification.NotificationSettingsModel
import com.example.assu_fe_app.domain.model.notification.NotificationTypeModel
import com.example.assu_fe_app.util.RetrofitResult
import com.example.assu_fe_app.util.apiHandler
import jakarta.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationService
) : NotificationRepository {

    override suspend fun toggle(type: NotificationTypeModel): RetrofitResult<NotificationSettingsModel> {
        return apiHandler(
            execute = { api.toggle(type.path) },
            mapper = { dto -> NotificationSettingsModel.from(dto.settings) }
        )
    }

    override suspend fun getSettings(): RetrofitResult<NotificationSettingsModel> =
        apiHandler(
            execute = { api.getSettings() },
            mapper  = { dto -> NotificationSettingsModel.from(dto.settings) }
        )
}