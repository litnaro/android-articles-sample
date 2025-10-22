package com.example.androidarticlessample.domain.usecase

import com.example.androidarticlessample.domain.model.Article
import com.example.androidarticlessample.domain.repository.ArticleRepository
import com.example.androidarticlessample.domain.util.AppException
import com.example.androidarticlessample.domain.util.Container
import com.example.androidarticlessample.domain.util.Period
import com.example.androidarticlessample.testutil.Samples
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

private class FakeRepo(private val articles: List<Article>) : ArticleRepository {
    override suspend fun getMostViewedArticles(period: Period) = Container.Success(articles)
    override suspend fun getArticleById(id: Long) =
        articles.firstOrNull { it.id == id }?.let { Container.Success(it) }
            ?: Container.Error(AppException.NotFound())
}

class GetMostViewedArticlesUseCaseTest {

    @Test
    fun `invoke() with valid period returns articles from repository`() = runTest {
        val repository = FakeRepo(listOf(Samples.article(id = 1L), Samples.article(id = 2L)))
        val useCase = GetMostViewedArticlesUseCase(repository)

        val result = useCase(Period.SEVEN_DAYS)
        require(result is Container.Success)
        assertEquals(2, result.data.size)
    }
}