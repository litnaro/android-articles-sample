package com.example.androidarticlessample.domain.usecase

import com.example.androidarticlessample.domain.model.Article
import com.example.androidarticlessample.domain.repository.ArticleRepository
import com.example.androidarticlessample.domain.util.Period
import com.example.androidarticlessample.domain.util.Container


/**
 * Use case for retrieving the most viewed articles.
 */
class GetMostViewedArticlesUseCase(
    private val repository: ArticleRepository
) {

    /**
     * Executes the use case.
     *
     * @param period The time range (1, 7, or 30 days).
     * @return [Container] with a list of [Article] or an error.
     */
    suspend operator fun invoke(period: Period): Container<List<Article>> {
        return repository.getMostViewedArticles(period)
    }
}