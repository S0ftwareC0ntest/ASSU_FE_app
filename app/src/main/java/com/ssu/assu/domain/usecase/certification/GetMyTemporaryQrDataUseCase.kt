package com.ssu.assu.domain.usecase.certification

import com.ssu.assu.data.dto.certification.response.TemporaryQrResponseDto
import com.ssu.assu.data.repository.certification.CertificationRepository
import com.ssu.assu.util.RetrofitResult
import javax.inject.Inject

class GetMyTemporaryQrDataUseCase @Inject constructor(
    private val repo: CertificationRepository
) {
    suspend operator fun invoke()
    : RetrofitResult<List<TemporaryQrResponseDto>>{
        return repo.getMyTemporaryData()
    }
}