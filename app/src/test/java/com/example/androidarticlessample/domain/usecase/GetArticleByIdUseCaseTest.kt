package com.example.androidarticlessample.domain.usecase

import com.example.androidarticlessample.domain.model.Article
import com.example.androidarticlessample.domain.repository.ArticleRepository
import com.example.androidarticlessample.domain.util.AppException
import com.example.androidarticlessample.domain.util.Container
import com.example.androidarticlessample.domain.util.Period
import com.example.androidarticlessample.testutil.Samples
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

private class FakeRepo2(private val articles: List<Article>) : ArticleRepository {
    override suspend fun getMostViewedArticles(period: Period) = Container.Success(articles)
    override suspend fun getArticleById(id: Long) =
        articles.firstOrNull { it.id == id }?.let { Container.Success(it) }
            ?: Container.Error(AppException.NotFound())
}

class GetArticleByIdUseCaseTest {

    @Test
    fun `invoke() with null id returns Error Invalid`() = runTest {
        val useCase = GetArticleByIdUseCase(FakeRepo2(emptyList()))
        val result = useCase(null)

        assertTrue(result is Container.Error && result.exception is AppException.Invalid)
    }

    @Test
    fun `invoke() with existing id returns Success`() = runTest {
        val useCase = GetArticleByIdUseCase(FakeRepo2(listOf(Samples.article(id = 10))))
        val result = useCase(10)

        assertTrue(result is Container.Success)
    }
}