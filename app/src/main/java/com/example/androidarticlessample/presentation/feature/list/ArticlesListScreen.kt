package com.example.androidarticlessample.presentation.feature.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.androidarticlessample.R
import com.example.androidarticlessample.domain.util.Period
import com.example.androidarticlessample.presentation.feature.list.ArticlesListViewModel.ArticlesListUiStateEvent
import com.example.androidarticlessample.presentation.model.ArticlePreview
import com.example.androidarticlessample.presentation.theme.ArticlesTheme
import com.example.androidarticlessample.presentation.widget.ErrorView
import com.example.androidarticlessample.presentation.widget.LoadingView

@Composable
fun ArticlesListScreen(
    navigateToDetails: (Long) -> Unit,
    viewModel: ArticlesListViewModel = hiltViewModel()
) {
    val screenState = viewModel.state.collectAsState()

    ProcessUiEvent(
        events = screenState.value.viewEvents,
        onViewEventProcessed = { viewModel.onEventProcessed(it) },
        navigateToDetails = navigateToDetails
    )

    LaunchedEffect(Unit) { viewModel.initialize() }

    ProcessViewState(
        state = screenState.value.viewState,
        onRetry = viewModel::retry,
        onItemClick = viewModel::onArticleClick,
        onQueryChange = viewModel::onQueryChange,
        onClearQuery = { viewModel.onQueryChange("") },
        onSelectPeriod = viewModel::onPeriodSelected
    )
}

@Composable
private fun ProcessUiEvent(
    events: List<ArticlesListUiStateEvent>,
    onViewEventProcessed: (ArticlesListUiStateEvent) -> Unit,
    navigateToDetails: (Long) -> Unit
) {
    events.firstOrNull()?.let {
        onViewEventProcessed(it)
        when(it) {
            is ArticlesListUiStateEvent.NavigateToDetails -> {
                navigateToDetails(it.articleId)
            }
        }
    }
}

@Composable
private fun ProcessViewState(
    state: ArticlesListViewModel.ArticlesListUiState,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onSelectPeriod: (Period) -> Unit,
    onRetry: () -> Unit,
    onItemClick: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            ArticlesListScreenTopBar(
                title = stringResource(R.string.article_list_screen_topbar_title),
                query = state.query,
                period = state.period,
                onQueryChange = onQueryChange,
                onClearQuery = onClearQuery,
                onSelectPeriod = onSelectPeriod
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("ArticlesList")
                    ) {
                        items(state.filteredArticles) { item ->
                            ArticleItem(
                                articlePreview = item,
                                modifier = Modifier
                                    .clickable(
                                        role = Role.Button,
                                        onClick = { onItemClick(item.id) }
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArticlesListScreenTopBar(
    title: String,
    query: String,
    period: Period,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onSelectPeriod: (Period) -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var isSearchMode by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Cyan)
            .statusBarsPadding()
            .heightIn(min = 72.dp)
            .padding(all = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        AnimatedContent(
            targetState = isSearchMode,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "TopBarAnimation"
        ) { searchMode ->

            if (searchMode) {
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    placeholder = { Text("Search…") },
                    leadingIcon = {
                        IconButton(onClick = { isSearchMode = false; onClearQuery() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close search"
                            )
                        }
                    },
                    trailingIcon = {
                        if (query.isNotBlank()) {
                            IconButton(onClick = onClearQuery) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear"
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null //TODO content description
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = { isSearchMode = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null //TODO content description
                        )
                    }

                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null //TODO content description
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            Period.entries.forEach { entry ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(text = entry.label())
                                            if (entry == period) {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                                    contentDescription = null //TODO content description
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        menuExpanded = false
                                        onSelectPeriod(entry)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Period.label(): String = when (this) {
    Period.ONE_DAY -> stringResource(R.string.period_one_day)
    Period.SEVEN_DAYS -> stringResource(R.string.period_seven_days)
    Period.THIRTY_DAYS -> stringResource(R.string.period_thirty_days)
}

@Preview(showSystemUi = true)
@Composable
fun ArticlesListScreenPreview() {
    ArticlesTheme {
        ProcessViewState(
            state = ArticlesListViewModel.ArticlesListUiState(
                filteredArticles = listOf(
                    ArticlePreview(
                        id = 0,
                        title = stringResource(R.string.lorem_ipsum),
                        byline = stringResource(R.string.lorem_ipsum),
                        publishedDate = "2021-17-17",
                        thumbnailUrl = ""
                    ),
                    ArticlePreview(
                        id = 0,
                        title = stringResource(R.string.lorem_ipsum),
                        byline = stringResource(R.string.lorem_ipsum),
                        publishedDate = "2021-17-17",
                        thumbnailUrl = ""
                    )
                )
            ),
            onQueryChange = {},
            onRetry = {},
            onSelectPeriod = {},
            onItemClick = {},
            onClearQuery = {}
        )
    }
}