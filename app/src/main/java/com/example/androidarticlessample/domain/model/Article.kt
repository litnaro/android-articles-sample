package com.example.androidarticlessample.domain.model

data class Article(
    val id: Long,
    val title: String,
    val byline: String,
    val publishedDate: String,
    val abstractText: String,
    val url: String,
    val media: List<Media> = emptyList()
)
