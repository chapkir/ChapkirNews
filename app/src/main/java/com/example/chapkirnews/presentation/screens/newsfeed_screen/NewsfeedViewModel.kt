package com.example.chapkirnews.presentation.screens.newsfeed_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chapkirnews.domain.model.Article
import com.example.chapkirnews.domain.usecase.all_news.GetNewsUseCase
import com.example.chapkirnews.domain.usecase.favorites_news.AddArticleToFavoritesUseCase
import com.example.chapkirnews.domain.usecase.favorites_news.GetFavoriteNewsUseCase
import com.example.chapkirnews.domain.usecase.favorites_news.RemoveArticleFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsfeedViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val addToFavorites: AddArticleToFavoritesUseCase,
    private val removeFromFavorites: RemoveArticleFromFavoritesUseCase,
    private val getFavorites: GetFavoriteNewsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsfeedUiState())
    val uiState: StateFlow<NewsfeedUiState> = _uiState.asStateFlow()

    init {
        loadNews()
        observeFavorites()
    }

    private fun loadNews(query: String = "А", page: Int = 1, pageSize: Int = 20) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val news = getNewsUseCase(query, page, pageSize)
                val currentFavorites = _uiState.value.favorites.map { it.url }

                val updated = news.map { it.copy(isFavorite = it.url in currentFavorites) }

                _uiState.update { it.copy(news = updated, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Ошибка") }
            }
        }
    }

    fun toggleFavorite(article: Article) {
        viewModelScope.launch {
            if (article.isFavorite) {
                removeFromFavorites(article)
            } else {
                addToFavorites(article)
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavorites().collect { favorites ->
                val favoriteUrls = favorites.map { it.url }
                _uiState.update { state ->
                    state.copy(
                        favorites = favorites,
                        news = state.news.map {
                            it.copy(isFavorite = it.url in favoriteUrls)
                        }
                    )
                }
            }
        }
    }
}

