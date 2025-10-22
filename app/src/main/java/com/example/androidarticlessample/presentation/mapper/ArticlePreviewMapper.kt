package com.example.androidarticlessample.presentation.mapper

import com.example.androidarticlessample.domain.model.Article
import com.example.androidarticlessample.presentation.model.ArticlePreview

class ArticlePreviewMapper {

    fun map(list: List<Article>): List<ArticlePreview> = list.map { map(it) }

    fun map(article: Article): ArticlePreview = ArticlePreview(
        id = article.id,
        title = article.title,
        byline = article.byline,
        publishedDate = article.publishedDate,
        thumbnailUrl = article.media.firstOrNull()?.url
    )
}