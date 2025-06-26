package com.example.chapkirnews.presentation.screens.newsfeed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chapkirnews.presentation.components.news_card.NewsCard

@Composable
fun NewsfeedScreen(
    viewModel: NewsfeedViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    when {
        state.isLoading -> CircularProgressIndicator()
        state.error != null -> Text("Ошибка: ${state.error}")
        else ->
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
                        content = article.content
                    )
                }
            }
    }
}
