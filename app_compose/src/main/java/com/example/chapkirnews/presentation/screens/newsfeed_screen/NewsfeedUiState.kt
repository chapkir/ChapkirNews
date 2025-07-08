package com.example.chapkirnews.presentation.screens.newsfeed_screen

import com.example.domain.model.Article

data class NewsfeedUiState(
    val isLoading: Boolean = false,
    val news: List<Article> = emptyList(),
    val favorites: List<Article> = emptyList(),
    val error: String? = null,
    val message: String? = null,
    val isSearchActive: Boolean = false,
    val searchQuery: String = "",
)