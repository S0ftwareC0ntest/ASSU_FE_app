package com.ssu.assu.domain.usecase.dashboard


import com.ssu.assu.data.dto.dashboard.response.TodayBestDto
import com.ssu.assu.data.dto.dashboard.response.WeeklyRankResponseDto
import com.ssu.assu.data.repository.dashboard.PartnerDashboardRepository
import com.ssu.assu.data.repository.store.StoreRepository
import com.ssu.assu.domain.model.dashboard.StampRankingModel
import com.ssu.assu.util.RetrofitResult
import jakarta.inject.Inject


class GetTodayBestStoreUseCase @Inject constructor(
    private val repo: PartnerDashboardRepository
) {
    suspend operator fun invoke(): RetrofitResult<TodayBestDto> {
        return repo.getTodayBestStore()
    }
}

class GetPartnerWeeklyRankUseCase @Inject constructor(
    private val repo: PartnerDashboardRepository
) {
    suspend operator fun invoke(): RetrofitResult<WeeklyRankResponseDto> {
        return repo.getWeeklyRank()
    }
}

class GetPartnerWeeklyRankListUseCase @Inject constructor(
    private val repo: PartnerDashboardRepository
) {
    suspend operator fun invoke(): RetrofitResult<List<WeeklyRankResponseDto>> {
        return repo.getWeeklyRankList()
    }
}

class GetStampRankingUseCase @Inject constructor(
    private val repo: PartnerDashboardRepository
) {
    suspend operator fun invoke(): RetrofitResult<List<StampRankingModel>> {
        return repo.getStampRanking()
    }
}