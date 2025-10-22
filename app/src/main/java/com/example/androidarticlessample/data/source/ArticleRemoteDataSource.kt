package com.example.androidarticlessample.data.source

import com.example.androidarticlessample.core.network.NyTimesApi
import com.example.androidarticlessample.data.dto.MostViewedResponse

class ArticleRemoteDataSource(
    private val api: NyTimesApi,
    private val apiKey: String
) {
    suspend fun mostViewed(periodDays: Int): MostViewedResponse =
        api.getMostViewed(periodDays, apiKey)
}