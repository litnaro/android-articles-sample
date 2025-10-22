package com.example.androidarticlessample.data.dto

import com.google.gson.annotations.SerializedName

data class MediaDto(
    val type: String? = null,
    val subtype: String? = null,
    val caption: String? = null,
    @SerializedName("media-metadata")
    val mediaMetadata: List<MediaMetadataDto>? = null
)
