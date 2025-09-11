package com.example.assu_fe_app.ui.deviceToken

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assu_fe_app.data.manager.TokenManager
import com.example.assu_fe_app.domain.usecase.deviceToken.RegisterDeviceTokenUseCase
import com.example.assu_fe_app.util.RetrofitResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DeviceTokenViewModel @Inject constructor(
    private val registerDeviceToken: RegisterDeviceTokenUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    sealed interface UiState {
        data object Idle : UiState
        data object Loading : UiState
        data class Success(val msg: String) : UiState
        data class Fail(val code: Int, val msg: String) : UiState
        data class Error(val msg: String) : UiState
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    fun register(token: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            when (val r = registerDeviceToken(token)) {
                is RetrofitResult.Success -> {
                    // 서버에서 반환된 tokenId를 저장
                    val tokenId = r.data.toIntOrNull()
                    if (tokenId != null) {
                        tokenManager.saveDeviceTokenId(tokenId)
                    }
                    _uiState.value = UiState.Success(r.data)
                }
                is RetrofitResult.Fail    -> _uiState.value = UiState.Fail(r.statusCode, r.message)
                is RetrofitResult.Error   -> _uiState.value = UiState.Error(r.exception.message ?: "unknown error")
            }
        }
    }
}