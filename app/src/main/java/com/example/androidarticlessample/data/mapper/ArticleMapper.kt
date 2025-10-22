package com.example.androidarticlessample.data.mapper

import com.example.androidarticlessample.data.dto.ArticleDto
import com.example.androidarticlessample.data.dto.MediaDto
import com.example.androidarticlessample.data.dto.MostViewedResponse
import com.example.androidarticlessample.domain.model.Article
import com.example.androidarticlessample.domain.model.Media

class ArticleMapper {

    fun fromResponse(dto: MostViewedResponse): List<Article> =
        dto.results.orEmpty().map(::fromDto)

    private fun fromDto(dto: ArticleDto): Article =
        Article(
            id = dto.id ?: 0L,
            title = dto.title.orEmpty(),
            byline = dto.byline ?: "",
            publishedDate = dto.publishedDate.orEmpty(),
            abstractText = dto.abstract.orEmpty(),
            url = dto.url.orEmpty(),
            media = dto.media.orEmpty().flatMap(::mapMedia)
        )

    private fun mapMedia(m: MediaDto): List<Media> =
        m.mediaMetadata.orEmpty().map { meta ->
            Media(
                type = m.type.orEmpty(),
                caption = m.caption,
                url = meta.url.orEmpty()
            )
        }
}