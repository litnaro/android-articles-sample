package com.example.androidarticlessample.testutil

import com.example.androidarticlessample.core.network.NyTimesApi
import com.example.androidarticlessample.data.dto.ArticleDto
import com.example.androidarticlessample.data.dto.MediaDto
import com.example.androidarticlessample.data.dto.MediaMetadataDto
import com.example.androidarticlessample.data.dto.MostViewedResponse
import kotlinx.coroutines.delay

class FakeNyTimesApi(
    private val results: List<ArticleDto> = emptyList(),
    private val delayMs: Long = 0L
) : NyTimesApi {
    override suspend fun getMostViewed(period: Int, apiKey: String): MostViewedResponse {
        if (delayMs > 0) delay(delayMs)
        return MostViewedResponse(
            status = "OK",
            results = results
        )
    }
}

object DtoFactory {
    fun articleDto(id: Long = 1L, title: String = "Hello", byline: String? = "By John", date: String = "2025-10-21", url: String = "https://ex"): ArticleDto =
        ArticleDto(
            id = id, title = title, byline = byline, publishedDate = date, url = url,
            media = listOf(
                MediaDto(
                    type = "image", subtype = null, caption = null,
                    mediaMetadata = listOf(
                        MediaMetadataDto(
                            url = "https://img",
                            format = "Standard Thumbnail",
                            height = 75,
                            width = 75
                        )
                    )
                )
            )
        )
}