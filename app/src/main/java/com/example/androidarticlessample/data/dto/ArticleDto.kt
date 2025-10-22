package com.example.androidarticlessample.data.dto

import com.google.gson.annotations.SerializedName

data class ArticleDto(
    val id: Long? = null,
    val url: String? = null,
    val title: String? = null,
    val byline: String? = null,
    val abstract: String? = null,
    @SerializedName("published_date")
    val publishedDate: String? = null,
    val media: List<MediaDto>? = null
)
