package com.example.chapkirnews.presentation.screens.favorites_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chapkirnews.R
import com.example.domain.model.Article
import com.example.chapkirnews.presentation.components.ErrorBlock
import com.example.chapkirnews.presentation.components.news_card.NewsCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onArticleClick: (Article) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 14.dp, bottom = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .padding(start = 12.dp, end = 16.dp)
                    .size(28.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "menu",
                    modifier = Modifier.size(22.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = "Избранное",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(50.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        strokeWidth = 7.dp,
                    )
                }

                state.error != null -> {
                    ErrorBlock(
                        message = state.error!!,
                        icon = R.drawable.ic_cross_small,
                        modifier = Modifier
                            .fillMaxWidth()
                            .imePadding()
                            .align(Alignment.Center)
                            .padding(horizontal = 25.dp)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.favorites, key = { it.url }) { article ->
                            val dismissState = rememberSwipeToDismissBoxState(
                                positionalThreshold = { totalDistance -> totalDistance * 0.7f },
                                confirmValueChange = { value ->
                                    when (value) {
                                        SwipeToDismissBoxValue.EndToStart -> {
                                            coroutineScope.launch {
                                                delay(100)
                                                viewModel.toggleFavorite(article)
                                            }
                                            true
                                        }

                                        SwipeToDismissBoxValue.StartToEnd -> {
                                            false
                                        }

                                        else -> false
                                    }
                                }
                            )

                            SwipeToDismissBox(
                                state = dismissState,
                                backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(shape = RoundedCornerShape(14.dp))
                                            .background(MaterialTheme.colorScheme.secondary)
                                            .padding(horizontal = 14.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxHeight(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                painter = painterResource(
                                                    id = R.drawable.ic_bookmark_slash
                                                ),
                                                contentDescription = "unFav",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier
                                                    .padding(bottom = 50.dp)
                                                    .size(30.dp)
                                            )
                                            Text(
                                                text = "Убрать\nиз избранных",
                                                color = MaterialTheme.colorScheme.primary,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                NewsCard(
                                    title = article.title,
                                    imageUrl = article.imageUrl,
                                    author = article.author,
                                    description = article.description,
                                    publishedAt = article.publishedAt,
                                    isFavorite = article.isFavorite,
                                    onToggleFavorite = { viewModel.toggleFavorite(article) },
                                    onArticleClick = { onArticleClick(article) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}