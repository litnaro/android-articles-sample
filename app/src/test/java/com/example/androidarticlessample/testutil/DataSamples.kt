package com.example.androidarticlessample.testutil

import com.example.androidarticlessample.domain.model.Article
import com.example.androidarticlessample.domain.model.Media

object Samples {
    fun article(
        id: Long = 1L,
        title: String = "Title",
        byline: String = "Byline",
        date: String = "2025-10-22",
        url: String = "https://example",
        mediaUrl: String? = "https://img"
    ) = Article(
        id = id,
        title = title,
        byline = byline,
        publishedDate = date,
        abstractText = "Abstract",
        url = url,
        media = mediaUrl?.let { listOf(Media(type = "image", caption = null, url = it)) }
            ?: emptyList()
    )
}