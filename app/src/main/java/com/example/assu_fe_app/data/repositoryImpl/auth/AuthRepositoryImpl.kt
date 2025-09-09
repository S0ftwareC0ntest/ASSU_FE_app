package com.example.assu_fe_app.data.repositoryImpl

import com.example.assu_fe_app.data.dto.auth.BaseResponseDto
import com.example.assu_fe_app.data.dto.auth.CommonLoginRequestDto
import com.example.assu_fe_app.data.dto.auth.CommonLoginResponseDto
import com.example.assu_fe_app.data.dto.auth.StudentLoginRequestDto
import com.example.assu_fe_app.data.dto.auth.StudentLoginResponseDto
import com.example.assu_fe_app.data.service.AuthService
import com.example.assu_fe_app.domain.model.auth.LoginModel
import com.example.assu_fe_app.domain.repository.AuthRepository
import com.example.assu_fe_app.util.RetrofitResult
import com.example.assu_fe_app.util.apiHandler
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {
    
    override suspend fun studentLogin(request: StudentLoginRequestDto): RetrofitResult<LoginModel> {
        return apiHandler(
            execute = { authService.studentLogin(request) },
            mapper = { response -> response.toModel() }
        )
    }
    
    override suspend fun commonLogin(request: CommonLoginRequestDto): RetrofitResult<LoginModel> {
        return apiHandler(
            execute = { authService.commonLogin(request) },
            mapper = { response -> response.toModel() }
        )
    }
}
