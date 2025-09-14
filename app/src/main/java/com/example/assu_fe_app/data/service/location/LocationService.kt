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
        @Query("minLng") minLng: Double,
        @Query("minLat") minLat: Double,
        @Query("maxLng") maxLng: Double,
        @Query("maxLat") maxLat: Double
    ): BaseResponse<List<StoreMapResponseDto>>

    /** ADMIN 토큰 → Partner 리스트 */
    @GET("/map/nearby")
    suspend fun getPartners(
        @Query("minLng") minLng: Double,
        @Query("minLat") minLat: Double,
        @Query("maxLng") maxLng: Double,
        @Query("maxLat") maxLat: Double
    ): BaseResponse<List<PartnerMapResponseDto>>

    /** PARTNER 토큰 → Admin 리스트 */
    @GET("/map/nearby")
    suspend fun getAdmins(
        @Query("minLng") minLng: Double,
        @Query("minLat") minLat: Double,
        @Query("maxLng") maxLng: Double,
        @Query("maxLat") maxLat: Double
    ): BaseResponse<List<AdminMapResponseDto>>
}