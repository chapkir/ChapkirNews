package com.example.chapkirnews.presentation.screens.favorites_screen

import com.example.domain.model.Article

data class FavoritesUiState(
    val favorites: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)