package com.ssu.assu.data.service.user

import com.ssu.assu.data.dto.BaseResponse
import com.ssu.assu.data.dto.dashboard.response.StampRankingResultDto
import com.ssu.assu.data.dto.dashboard.response.TodayBestDto
import com.ssu.assu.data.dto.user.home.GetUsablePartnershipResponseDto
import com.ssu.assu.data.dto.user.home.StampResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface UserHomeService {

    @GET("/students/stamp")
    suspend fun getStampCount(): BaseResponse<StampResponseDto>

    @GET("/store/best")
    suspend fun getTodayBestStores(): BaseResponse<TodayBestDto>

    @GET("/store/stamp-ranking")
    suspend fun getStampRanking(): BaseResponse<StampRankingResultDto>

    @GET("/students/usable")
    suspend fun getUsablePartnership(
        @Query("all") all: Boolean
    ): BaseResponse<List<GetUsablePartnershipResponseDto>>
}