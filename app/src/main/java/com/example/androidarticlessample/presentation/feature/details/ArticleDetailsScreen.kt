package com.example.androidarticlessample.presentation.feature.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.androidarticlessample.domain.model.Article
import com.example.androidarticlessample.presentation.feature.details.ArticleDetailsViewModel.ArticleDetailsUiEvent
import com.example.androidarticlessample.presentation.widget.ErrorView
import com.example.androidarticlessample.presentation.widget.LoadingView

@Composable
fun ArticleDetailsScreen(
    articleId: Long?,
    onBack: () -> Unit = {},
    viewModel: ArticleDetailsViewModel = hiltViewModel()
) {
    val screenState = viewModel.state.collectAsState()

    ProcessUiEvent(
        events = screenState.value.viewEvents,
        onViewEventProcessed = { viewModel.onEventProcessed(it) },
        onBack = onBack
    )

    LaunchedEffect(Unit) { viewModel.initialize(articleId) }

    ProcessViewState(
        state = screenState.value.viewState,
        onRetry = viewModel::retry,
        onBack = viewModel::exit
    )
}

@Composable
private fun ProcessUiEvent(
    events: List<ArticleDetailsUiEvent>,
    onViewEventProcessed: (ArticleDetailsUiEvent) -> Unit,
    onBack: () -> Unit
) {
    events.firstOrNull()?.let {
        onViewEventProcessed(it)
        when(it) {
            is ArticleDetailsUiEvent.NavigateBack -> {
                onBack()
            }
        }
    }
}

@Composable
private fun ProcessViewState(
    state: ArticleDetailsViewModel.ArticleDetailsUiState,
    onRetry: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = { ArticleDetailsScreenTopBar(onBack) },
        modifier = Modifier.testTag("DetailsScreen")
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {
            when {
                state.isLoading -> {
                    LoadingView(Modifier.fillMaxSize())
                }
                state.error != null -> {
                    ErrorView(
                        message = state.error.message ?: "Something went wrong",
                        onRetry = onRetry,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    ArticleDetailsContent(
                        article = state.article,
                        modifier = Modifier.testTag("DetailsContent")
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleDetailsContent(
    article: Article?,
    modifier: Modifier = Modifier
) {
    if (article == null) return

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val thumb = article.media.firstOrNull()?.url
        if (!thumb.isNullOrBlank()) {
            AsyncImage(
                model = thumb,
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Text(
            text = article.title,
            style = MaterialTheme.typography.titleLarge,
        )

        article.byline?.takeIf { it.isNotBlank() }?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = article.publishedDate,
            style = MaterialTheme.typography.labelSmall
        )

        if (article.abstractText.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = article.abstractText,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun ArticleDetailsScreenTopBar(
    onBack: (() -> Unit),
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Cyan)
            .statusBarsPadding()
            .heightIn(min = 72.dp)
            .padding(all = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null, //TODO content description
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .clickable(
                    role = Role.Button,
                    onClick = { onBack() }
                )
        )
    }
}