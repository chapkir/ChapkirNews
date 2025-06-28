package com.example.chapkirnews.presentation.screens.newsfeed_screen

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.chapkirnews.R
import com.example.chapkirnews.domain.model.Article
import com.example.chapkirnews.presentation.components.ErrorBlock
import com.example.chapkirnews.presentation.components.news_card.NewsCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsfeedScreen(
    viewModel: NewsfeedViewModel = hiltViewModel(),
    onArticleClick: (Article) -> Unit
) {
    val news = viewModel.newsPagingFlow.collectAsLazyPagingItems()
    val state by viewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()

    val loadState = news.loadState

    val isInitialLoading = loadState.source.refresh is LoadState.Loading && news.itemCount == 0
    val isPullToRefresh = loadState.refresh is LoadState.Loading && !isInitialLoading

    val stateRefresh = rememberPullToRefreshState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            focusManager.clearFocus()
        }
    }

//    LaunchedEffect(news) {
//        listState.scrollToItem(0)
//    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            androidx.compose.animation.AnimatedVisibility(
                visible = state.isSearchActive,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 200)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = 200)
                )
            ) {
                SearchBar(
                    onClose = { viewModel.closeSearch() },
                    query = state.searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    focusManager = focusManager
                )
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = !state.isSearchActive,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 200))
            ) {
                NewsfeedTopBar(
                    onSearchClick = { viewModel.openSearch() }
                )
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            PullToRefreshBox(
                isRefreshing = isPullToRefresh,
                onRefresh = { news.refresh() },
                state = stateRefresh,
                indicator = {
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = isPullToRefresh,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        color = MaterialTheme.colorScheme.primary,
                        state = stateRefresh
                    )
                }
            ) {
                when {
                    isInitialLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(45.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 8.dp,
                            )
                        }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val e = (loadState.refresh as LoadState.Error).error
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            ErrorBlock(
                                message = "Ошибка загрузки новостей." +
                                        " Сервис плохо работает в России, попробуй включить VPN. "
                                        + e.message,
                                icon = R.drawable.ic_cross_small,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center)
                                    .imePadding()
                                    .padding(horizontal = 25.dp)
                            )
                        }
                    }

                    news.itemCount == 0 -> {
                        ErrorBlock(
                            message = "По Вашему запросу новостей нет." +
                                    " Попробуйте найти что-то другое!",
                            icon = R.drawable.ic_cross_small,
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                                .imePadding()
                                .padding(horizontal = 25.dp)
                        )
                    }

                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(news.itemCount) { index ->
                                news[index]?.let { article ->
                                    NewsCard(
                                        title = article.title,
                                        imageUrl = article.imageUrl,
                                        author = article.author,
                                        description = article.description,
                                        publishedAt = article.publishedAt,
                                        content = article.content,
                                        isFavorite = article.isFavorite,
                                        onToggleFavorite = { viewModel.toggleFavorite(article) },
                                        onArticleClick = { onArticleClick(article) }
                                    )
                                }
                            }
                            if (loadState.append is LoadState.Loading) {
                                item() {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            color = MaterialTheme.colorScheme.primary,
                                            strokeWidth = 4.dp,
                                        )
                                    }
                                }
                            }

                            if (loadState.append is LoadState.Error) {
                                val e = (loadState.append as LoadState.Error).error
                                item() {
                                    Button(
                                        onClick = { news.retry() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Text(
                                            text = "Повторить: ${e.message}",
                                            modifier = Modifier.padding(vertical = 3.dp),
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            val showScrollToTopButton by remember {
                derivedStateOf { listState.firstVisibleItemIndex > 1 }
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = showScrollToTopButton,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier.size(58.dp),
                    shape = RoundedCornerShape(20.dp),
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_up),
                        contentDescription = "toTop",
                        modifier = Modifier.size(38.dp)
                    )
                }
            }
        }
    }
}
