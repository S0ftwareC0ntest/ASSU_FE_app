package com.ssu.assu.data.repository.dashboard

import com.ssu.assu.data.dto.dashboard.response.TodayBestDto
import com.ssu.assu.data.dto.dashboard.response.WeeklyRankResponseDto
import com.ssu.assu.domain.model.dashboard.StampRankingModel
import com.ssu.assu.util.RetrofitResult

interface PartnerDashboardRepository {
    suspend fun getTodayBestStore(): RetrofitResult<TodayBestDto>
    suspend fun getWeeklyRank(): RetrofitResult<WeeklyRankResponseDto>
    suspend fun getWeeklyRankList(): RetrofitResult<List<WeeklyRankResponseDto>>
    suspend fun getStampRanking(): RetrofitResult<List<StampRankingModel>>

}
