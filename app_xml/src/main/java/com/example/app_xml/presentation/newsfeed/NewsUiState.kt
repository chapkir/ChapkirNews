package com.example.app_xml.presentation.newsfeed

import com.example.domain.model.Article

data class NewsUiState(
    val isLoading: Boolean = false,
    val news: List<Article> = emptyList(),
    val favorites: List<Article> = emptyList(),
    val error: String? = null,
    val message: String? = null,
    val isSearchActive: Boolean = false,
    val searchQuery: String = "",
)