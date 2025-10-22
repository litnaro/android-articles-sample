package com.example.androidarticlessample.presentation.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidarticlessample.domain.usecase.GetMostViewedArticlesUseCase
import com.example.androidarticlessample.domain.util.AppException
import com.example.androidarticlessample.domain.util.Container
import com.example.androidarticlessample.domain.util.Period
import com.example.androidarticlessample.presentation.mapper.ArticlePreviewMapper
import com.example.androidarticlessample.presentation.model.ArticlePreview
import com.example.androidarticlessample.presentation.util.BaseViewModelWithState
import com.example.androidarticlessample.presentation.util.ViewModelWithState
import com.example.androidarticlessample.presentation.util.add
import com.example.androidarticlessample.presentation.util.asInitialState
import com.example.androidarticlessample.presentation.util.with
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesListViewModel @Inject constructor(
    private val getMostViewedArticlesUseCase: GetMostViewedArticlesUseCase,
    private val mapper: ArticlePreviewMapper
) : ViewModel(),
    ViewModelWithState<ArticlesListViewModel.ArticlesListUiState, ArticlesListViewModel.ArticlesListUiStateEvent> by BaseViewModelWithState(
        initialState = ArticlesListUiState().asInitialState()
    ) {
    data class ArticlesListUiState(
        val allArticles: List<ArticlePreview> = emptyList(),
        val filteredArticles: List<ArticlePreview> = emptyList(),
        val query: String = "",
        val period: Period = Period.SEVEN_DAYS,
        val isLoading: Boolean = false,
        val error: AppException? = null
    )

    sealed class ArticlesListUiStateEvent {
        data class NavigateToDetails(val articleId: Long) : ArticlesListUiStateEvent()
    }

    private var isInitialized = false

    fun initialize() {
        if (isInitialized) return
        isInitialized = true
        load(Period.SEVEN_DAYS)
    }

    fun load(period: Period) {
        updateState {
            this with viewState.copy(
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            when (val container = getMostViewedArticlesUseCase(period)) {
                is Container.Success -> {
                    val previews = mapper.map(container.data)
                    updateState {
                        this with viewState.copy(
                            allArticles = previews,
                            filteredArticles = applyFilter(previews, this.viewState.query),
                            period = period,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Container.Error -> {
                    updateState {
                        this with viewState.copy(
                            isLoading = false,
                            error = container.exception
                        )
                    }
                }
                is Container.Pending -> {
                    // Ignore
                }
            }
        }
    }

    fun retry() {
        load(Period.SEVEN_DAYS)
    }

    fun onArticleClick(id: Long) {
        updateState { this add ArticlesListUiStateEvent.NavigateToDetails(id) }
    }

    fun onQueryChange(query: String) {
        updateState {
            this with viewState.copy(
                query = query,
                filteredArticles = applyFilter(this.viewState.allArticles, query)
            )
        }
    }

    fun onPeriodSelected(newPeriod: Period) {
        if (state.value.viewState.period == newPeriod) return
        load(newPeriod)
    }

    private fun applyFilter(source: List<ArticlePreview>, query: String): List<ArticlePreview> {
        if (query.isBlank()) return source
        val key = query.trim().lowercase()
        return source.filter { item ->
            item.title.lowercase().contains(key) ||
                    (item.byline?.lowercase()?.contains(key) == true)
        }
    }

}