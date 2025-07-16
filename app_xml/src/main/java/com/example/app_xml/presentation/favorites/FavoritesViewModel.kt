package com.example.app_xml.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Article
import com.example.domain.usecase.favorites_news.AddArticleToFavoritesUseCase
import com.example.domain.usecase.favorites_news.GetFavoriteNewsUseCase
import com.example.domain.usecase.favorites_news.RemoveArticleFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavorites: GetFavoriteNewsUseCase,
    private val addToFavorites: AddArticleToFavoritesUseCase,
    private val removeFromFavorites: RemoveArticleFromFavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData(FavoritesUiState())
    val uiState: LiveData<FavoritesUiState> = _uiState

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            _uiState.postValue(_uiState.value?.copy(isLoading = true, error = null))
            try {
                getFavorites().collect { articles ->
                    if (articles.isEmpty()) {
                        _uiState.postValue(
                            FavoritesUiState(
                                favorites = emptyList(),
                                isLoading = false,
                                error = "Кажется, Вы ничего не сохраняли. Листайте ленту и добавляйте понравившиеся!"
                            )
                        )
                    } else {
                        _uiState.postValue(
                            FavoritesUiState(
                                favorites = articles.reversed().map { it.copy(isFavorite = true) },
                                isLoading = false,
                                error = null
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.postValue(
                    FavoritesUiState(
                        favorites = emptyList(),
                        isLoading = false,
                        error = e.message ?: "Ошибка при загрузке избранного"
                    )
                )
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