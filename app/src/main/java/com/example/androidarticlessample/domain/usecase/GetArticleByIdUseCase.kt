package com.example.androidarticlessample.domain.usecase

import com.example.androidarticlessample.domain.model.Article
import com.example.androidarticlessample.domain.repository.ArticleRepository
import com.example.androidarticlessample.domain.util.AppException
import com.example.androidarticlessample.domain.util.Container

/**
 * Use case for retrieving a single article by its ID.
 */
class GetArticleByIdUseCase(
    private val repository: ArticleRepository
) {

    /**
     * Executes the use case to retrieve an article by its ID.
     *
     * If [id] is null, returns [Container.Error] with [AppException.Invalid].
     *
     * @param id The unique ID of the article to load, or null if not provided.
     * @return [Container] containing the [Article] on success, or an error otherwise.
     */
    suspend operator fun invoke(id: Long?): Container<Article> {
        if (id == null) {
            return Container.Error(AppException.Invalid("Article ID cannot be null"))
        }
        return repository.getArticleById(id)
    }
}