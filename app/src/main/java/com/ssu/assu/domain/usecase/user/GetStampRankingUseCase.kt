package com.ssu.assu.domain.usecase.user

import com.ssu.assu.data.repository.user.UserHomeRepository
import com.ssu.assu.domain.model.dashboard.StampRankingModel
import com.ssu.assu.util.RetrofitResult
import jakarta.inject.Inject

class GetStampRankingUseCase @Inject constructor(
    private val repo: UserHomeRepository
) {
    suspend operator fun invoke(): RetrofitResult<List<StampRankingModel>> {
        return repo.getStampRanking()
    }
}
