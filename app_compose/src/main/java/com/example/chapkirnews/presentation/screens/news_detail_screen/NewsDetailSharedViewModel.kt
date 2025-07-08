package com.example.chapkirnews.presentation.screens.news_detail_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Article
import com.example.domain.usecase.favorites_news.AddArticleToFavoritesUseCase
import com.example.domain.usecase.favorites_news.RemoveArticleFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailSharedViewModel @Inject constructor(
    private val addToFavorites: AddArticleToFavoritesUseCase,
    private val removeFromFavorites: RemoveArticleFromFavoritesUseCase
) : ViewModel() {

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }

    fun clear() {
        _selectedArticle.value = null
    }

    fun toggleFavorite(article: Article) {
        viewModelScope.launch {
            if (article.isFavorite) {
                removeFromFavorites(article)
                _selectedArticle.value = article.copy(isFavorite = false)
            } else {
                addToFavorites(article)
                _selectedArticle.value = article.copy(isFavorite = true)
            }
        }
    }
}