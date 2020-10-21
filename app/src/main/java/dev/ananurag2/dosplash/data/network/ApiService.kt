package dev.ananurag2.dosplash.data.network

import dev.ananurag2.dosplash.model.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * created by ankur on 21/10/20
 */
interface ApiService {

    @GET("photos")
    suspend fun getLatestPhotos(
        @Query("page") pageNum: Int,
        @Query("client_id") clientId: String
    ): Response<List<ImageResponse>>

    @GET("photos/random")
    suspend fun getRandomPhoto(
        @Query("client_id") clientId: String
    ): Response<ImageResponse>
}