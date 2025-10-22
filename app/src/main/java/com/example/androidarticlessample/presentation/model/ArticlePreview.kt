package com.example.androidarticlessample.presentation.model

data class ArticlePreview(
    val id: Long,
    val title: String,
    val byline: String?,
    val thumbnailUrl: String?,
    val publishedDate: String
)