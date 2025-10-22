package com.example.androidarticlessample.presentation.feature.details

import com.example.androidarticlessample.domain.repository.ArticleRepository
import com.example.androidarticlessample.domain.usecase.GetArticleByIdUseCase
import com.example.androidarticlessample.domain.util.AppException
import com.example.androidarticlessample.domain.util.Container
import com.example.androidarticlessample.domain.util.Period
import com.example.androidarticlessample.testutil.MainDispatcherRule
import com.example.androidarticlessample.testutil.Samples
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class ArticleDetailsViewModelTest {

    @get:Rule
    val main = MainDispatcherRule(StandardTestDispatcher())

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `load() with existing id sets article in state`() = runTest {
        val article = Samples.article(id = 5)
        val repository = object : ArticleRepository {
            override suspend fun getMostViewedArticles(period: Period) = TODO()
            override suspend fun getArticleById(id: Long) = Container.Success(article)
        }
        val useCase = GetArticleByIdUseCase(repository)
        val viewModel = ArticleDetailsViewModel(useCase)

        viewModel.load(5)
        advanceUntilIdle()
        assertEquals(5L, viewModel.state.value.viewState.article?.id)
        assertFalse(viewModel.state.value.viewState.isLoading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `retry() after error triggers reload with last id`() = runTest {
        var succeed = false
        val repository = object : ArticleRepository {
            override suspend fun getMostViewedArticles(period: Period) = TODO()
            override suspend fun getArticleById(id: Long) =
                if (succeed) Container.Success(Samples.article(id))
                else Container.Error(AppException.Network())
        }
        val useCase = GetArticleByIdUseCase(repository)
        val viewModel = ArticleDetailsViewModel(useCase)

        viewModel.load(7)
        advanceUntilIdle()
        assertNull(viewModel.state.value.viewState.article)

        succeed = true
        viewModel.retry()
        advanceUntilIdle()
        assertEquals(7L, viewModel.state.value.viewState.article?.id)
    }
}

