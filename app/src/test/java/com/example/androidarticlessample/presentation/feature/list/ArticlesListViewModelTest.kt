package com.example.androidarticlessample.presentation.feature.list

import com.example.androidarticlessample.domain.repository.ArticleRepository
import com.example.androidarticlessample.domain.usecase.GetMostViewedArticlesUseCase
import com.example.androidarticlessample.domain.util.Container
import com.example.androidarticlessample.domain.util.Period
import com.example.androidarticlessample.presentation.mapper.ArticlePreviewMapper
import com.example.androidarticlessample.testutil.MainDispatcherRule
import com.example.androidarticlessample.testutil.Samples
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ArticlesListViewModelTest {

    @get:Rule
    val main = MainDispatcherRule(StandardTestDispatcher())

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `load() with repository success updates state with mapped previews`() = runTest {
        val articles = listOf(Samples.article(id = 1), Samples.article(id = 2, title = "Compose"))
        val repository = object : ArticleRepository {
            override suspend fun getMostViewedArticles(period: Period) = Container.Success(articles)
            override suspend fun getArticleById(id: Long) = TODO()
        }
        val useCase = GetMostViewedArticlesUseCase(repository)
        val viewModel = ArticlesListViewModel(useCase, ArticlePreviewMapper())

        viewModel.initialize()
        advanceUntilIdle()

        val state = viewModel.state.value.viewState
        assertEquals(2, state.allArticles.size)
    }
}