package com.example.chapkirnews.presentation.screens.favorites_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chapkirnews.domain.model.Article
import com.example.chapkirnews.domain.usecase.favorites_news.AddArticleToFavoritesUseCase
import com.example.chapkirnews.domain.usecase.favorites_news.GetFavoriteNewsUseCase
import com.example.chapkirnews.domain.usecase.favorites_news.RemoveArticleFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavorites: GetFavoriteNewsUseCase,
    private val addToFavorites: AddArticleToFavoritesUseCase,
    private val removeFromFavorites: RemoveArticleFromFavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                getFavorites().collect { articles ->
                    if (articles.isEmpty()) {
                        _uiState.update {
                            it.copy(
                                favorites = emptyList(),
                                isLoading = false,
                                error = "Кажется, Вы ничего не сохраняли." +
                                        " Листайте ленту и добавляйте сюда то, что понравится!"
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                favorites = articles.map { it.copy(isFavorite = true) },
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Ошибка при загрузке избранного"
                    )
                }
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
}
