package com.ssu.assu.domain.usecase.certification

import com.ssu.assu.data.dto.certification.request.TemporaryQrDataRequestDto
import com.ssu.assu.data.repository.certification.CertificationRepository
import com.ssu.assu.util.RetrofitResult
import javax.inject.Inject

class PostTemporaryQrDataUseCase @Inject constructor(
    private val repo: CertificationRepository
) {
    suspend operator fun invoke(
        request : TemporaryQrDataRequestDto
    ) : RetrofitResult<Unit> =
        repo.insertTemporaryQrData(request)

}