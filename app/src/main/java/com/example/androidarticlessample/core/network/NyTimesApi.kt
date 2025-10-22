package com.example.androidarticlessample.core.network

import com.example.androidarticlessample.data.dto.MostViewedResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NyTimesApi {
    @GET("svc/mostpopular/v2/viewed/{period}.json")
    suspend fun getMostViewed(
        @Path("period") period: Int,
        @Query("api-key") apiKey: String
    ): MostViewedResponse
}