package com.example.chapkirnews.presentation.screens.newsfeed

import com.example.chapkirnews.domain.model.Article

data class NewsfeedUiState(
    val isLoading: Boolean = false,
    val news: List<Article> = emptyList(),
    val error: String? = null
)