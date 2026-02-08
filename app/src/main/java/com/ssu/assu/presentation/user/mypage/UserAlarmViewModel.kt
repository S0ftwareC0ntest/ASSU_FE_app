package com.ssu.assu.presentation.user.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssu.assu.domain.model.notification.NotificationTypeModel
import com.ssu.assu.domain.usecase.notification.GetNotificationSettingsUseCase
import com.ssu.assu.domain.usecase.notification.ToggleNotificationUseCase
import com.ssu.assu.util.RetrofitResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*
import android.util.Log

@HiltViewModel
class UserAlarmViewModel @Inject constructor(
    private val getSettings: GetNotificationSettingsUseCase,
    private val toggle: ToggleNotificationUseCase
) : ViewModel() {

    private val _stampEnabled = MutableStateFlow(true)
    val stampEnabled: StateFlow<Boolean> = _stampEnabled.asStateFlow()

    val loading = MutableStateFlow(false)
    val error = MutableStateFlow<String?>(null)

    private var serverSnapshot: Boolean? = null

    fun load() {
        if (loading.value) return
        loading.value = true
        error.value = null

        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                getSettings()
            }
            when (res) {
                is RetrofitResult.Success -> {
                    val enabled = res.data.stamp ?: true
                    serverSnapshot = enabled
                    _stampEnabled.value = enabled
                    Log.d("UserAlarmViewModel", "Load success: stamp=$enabled")
                }
                is RetrofitResult.Fail -> {
                    error.value = res.message
                    Log.e("UserAlarmViewModel", "Load fail: ${res.message}")
                }
                is RetrofitResult.Error -> {
                    error.value = "네트워크 오류가 발생했어요. 잠시 후 다시 시도해주세요."
                    Log.e("UserAlarmViewModel", "Load error", res.exception)
                }
            }
            loading.value = false
        }
    }

    suspend fun commit() {
        if (loading.value) return
        loading.value = true
        error.value = null

        val before = serverSnapshot ?: _stampEnabled.value
        val after = _stampEnabled.value

        if (before != after) {
            try {
                withContext(Dispatchers.IO) {
                    val result = toggle(NotificationTypeModel.STAMP)
                    Log.d("UserAlarmViewModel", "Toggle result: $result")
                }
                serverSnapshot = after
                Log.d("UserAlarmViewModel", "Commit success: $after")
            } catch (t: Throwable) {
                error.value = "설정 저장 중 문제가 발생했어요. 다시 시도해주세요."
                Log.e("UserAlarmViewModel", "Commit error", t)
            }
        } else {
            Log.d("UserAlarmViewModel", "No change, skip commit")
        }
        loading.value = false
    }

    fun onStampClick(checked: Boolean) {
        _stampEnabled.value = checked
        Log.d("UserAlarmViewModel", "Stamp clicked: $checked")
        
        // 즉시 API 호출
        viewModelScope.launch {
            if (loading.value) return@launch
            loading.value = true
            
            try {
                withContext(Dispatchers.IO) {
                    val result = toggle(NotificationTypeModel.STAMP)
                    Log.d("UserAlarmViewModel", "Toggle API called: $result")
                }
                serverSnapshot = checked
                Log.d("UserAlarmViewModel", "Toggle success: $checked")
            } catch (t: Throwable) {
                error.value = "설정 저장 중 문제가 발생했어요. 다시 시도해주세요."
                Log.e("UserAlarmViewModel", "Toggle error", t)
                // 실패 시 원래 상태로 복원
                _stampEnabled.value = !checked
            } finally {
                loading.value = false
            }
        }
    }
}
