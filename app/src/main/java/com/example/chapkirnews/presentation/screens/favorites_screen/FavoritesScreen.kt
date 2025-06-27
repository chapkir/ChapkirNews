package com.example.chapkirnews.presentation.screens.favorites_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.chapkirnews.R
import com.example.chapkirnews.presentation.components.ErrorBlock
import com.example.chapkirnews.presentation.components.news_card.NewsCard
import com.example.chapkirnews.presentation.screens.newsfeed_screen.NewsfeedViewModel

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

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
                        .background(Color.Black),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.favorites) { article ->
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