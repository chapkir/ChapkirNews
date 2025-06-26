package com.example.chapkirnews.presentation.screens.newsfeed_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chapkirnews.presentation.components.news_card.NewsCard

@Composable
fun NewsfeedScreen(
    viewModel: NewsfeedViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            NewsfeedTopBar()
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                    Text(
                        text = "Ошибка загрузки: ${state.error}",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.news) { article ->
                            NewsCard(
                                title = article.title,
                                imageUrl = article.imageUrl,
                                author = article.author,
                                description = article.description,
                                publishedAt = article.publishedAt,
                                content = article.content,
                                isFavorite = article.isFavorite,
                                onToggleFavorite = { viewModel.toggleFavorite(article) }
                            )
                        }
                    }
                }
            }
        }
    }
}
