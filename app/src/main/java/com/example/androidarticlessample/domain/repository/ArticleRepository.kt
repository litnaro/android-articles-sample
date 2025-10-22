package com.example.androidarticlessample.domain.repository

import com.example.androidarticlessample.domain.model.Article
import com.example.androidarticlessample.domain.util.Period
import com.example.androidarticlessample.domain.util.Container

/**
 * Repository interface for fetching articles.
 */
interface ArticleRepository {

    /**
     * Fetches the most viewed articles for the given [period].
     *
     * @param period The time range (1, 7, or 30 days).
     * @return [Container] with data or error state.
     */
    suspend fun getMostViewedArticles(period: Period): Container<List<Article>>

    /**
     * Returns article by its [id].
     */
    suspend fun getArticleById(id: Long): Container<Article>
}