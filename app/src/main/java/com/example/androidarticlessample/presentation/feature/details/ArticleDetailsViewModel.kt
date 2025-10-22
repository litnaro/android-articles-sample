package com.example.androidarticlessample.presentation.feature.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidarticlessample.domain.model.Article
import com.example.androidarticlessample.domain.usecase.GetArticleByIdUseCase
import com.example.androidarticlessample.domain.util.AppException
import com.example.androidarticlessample.domain.util.Container
import com.example.androidarticlessample.presentation.util.BaseViewModelWithState
import com.example.androidarticlessample.presentation.util.ViewModelWithState
import com.example.androidarticlessample.presentation.util.add
import com.example.androidarticlessample.presentation.util.asInitialState
import com.example.androidarticlessample.presentation.util.with
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailsViewModel @Inject constructor(
    private val getArticleByIdUseCase: GetArticleByIdUseCase
) : ViewModel(),
    ViewModelWithState<ArticleDetailsViewModel.ArticleDetailsUiState, ArticleDetailsViewModel.ArticleDetailsUiEvent> by BaseViewModelWithState(
        initialState = ArticleDetailsUiState().asInitialState()
    )
{

    private var lastArticleId: Long? = null

    data class ArticleDetailsUiState(
        val article: Article? = null,
        val isLoading: Boolean = false,
        val error: AppException? = null
    )

    sealed class ArticleDetailsUiEvent {
        data object NavigateBack : ArticleDetailsUiEvent()
    }

    private var isInitialized = false

    fun initialize(articleId: Long?) {
        if (isInitialized) return
        isInitialized = true
        load(articleId)
    }

    fun load(articleId: Long?) {
        lastArticleId = articleId
        updateState {
            this with viewState.copy(
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            when (val container = getArticleByIdUseCase(articleId)) {
                is Container.Success -> {
                    updateState {
                        this with viewState.copy(
                            article = container.data,
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

    fun exit() {
        updateState { this add ArticleDetailsUiEvent.NavigateBack }
    }

    fun retry() {
        lastArticleId?.let { load(it) }
    }

}
