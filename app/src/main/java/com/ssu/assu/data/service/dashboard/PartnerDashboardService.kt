package com.ssu.assu.data.service.dashboard

import com.ssu.assu.data.dto.BaseResponse
import com.ssu.assu.data.dto.dashboard.response.StampRankingResultDto
import com.ssu.assu.data.dto.dashboard.response.TodayBestDto
import com.ssu.assu.data.dto.dashboard.response.WeeklyRankResponseDto
import retrofit2.http.GET

interface PartnerDashboardService {

    @GET("/store/best")
    suspend fun getTodayBestStore(): BaseResponse<TodayBestDto>

    @GET("/store/stamp-ranking")
    suspend fun getStampStoreRanking(): BaseResponse<StampRankingResultDto>

    @GET("/store/ranking")
    suspend fun getWeeklyRank(): BaseResponse<WeeklyRankResponseDto>

    @GET("/store/ranking/weekly")
    suspend fun getWeeklyRankList(): BaseResponse<List<WeeklyRankResponseDto>>
}