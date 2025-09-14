package com.example.assu_fe_app.data.service.location


import com.example.assu_fe_app.data.dto.BaseResponse
import com.example.assu_fe_app.data.dto.location.response.AdminMapResponseDto
import com.example.assu_fe_app.data.dto.location.response.PartnerMapResponseDto
import com.example.assu_fe_app.data.dto.location.response.StoreMapResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {

    /** STUDENT 토큰 → Store 리스트 */
    @GET("/map/nearby")
    suspend fun getStores(
        @Query("lng1") lng1: Double,
        @Query("lat1") lat1: Double,
        @Query("lng2") lng2: Double,
        @Query("lat2") lat2: Double,
        @Query("lng3") lng3: Double,
        @Query("lat3") lat3: Double,
        @Query("lng4") lng4: Double,
        @Query("lat4") lat4: Double
    ): BaseResponse<List<StoreMapResponseDto>>

    /** ADMIN 토큰 → Partner 리스트 */
    @GET("/map/nearby")
    suspend fun getPartners(
        @Query("lng1") lng1: Double,
        @Query("lat1") lat1: Double,
        @Query("lng2") lng2: Double,
        @Query("lat2") lat2: Double,
        @Query("lng3") lng3: Double,
        @Query("lat3") lat3: Double,
        @Query("lng4") lng4: Double,
        @Query("lat4") lat4: Double
    ): BaseResponse<List<PartnerMapResponseDto>>

    /** PARTNER 토큰 → Admin 리스트 */
    @GET("/map/nearby")
    suspend fun getAdmins(
        @Query("lng1") lng1: Double,
        @Query("lat1") lat1: Double,
        @Query("lng2") lng2: Double,
        @Query("lat2") lat2: Double,
        @Query("lng3") lng3: Double,
        @Query("lat3") lat3: Double,
        @Query("lng4") lng4: Double,
        @Query("lat4") lat4: Double
    ): BaseResponse<List<AdminMapResponseDto>>
}