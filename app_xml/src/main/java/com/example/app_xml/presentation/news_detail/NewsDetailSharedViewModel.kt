package com.example.app_xml.presentation.news_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Article
import com.example.domain.usecase.favorites_news.AddArticleToFavoritesUseCase
import com.example.domain.usecase.favorites_news.RemoveArticleFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailSharedViewModel @Inject constructor(
    private val addToFavorites: AddArticleToFavoritesUseCase,
    private val removeFromFavorites: RemoveArticleFromFavoritesUseCase
) : ViewModel() {

    private val _selectedArticle = MutableLiveData<Article?>()
    val selectedArticle: LiveData<Article?> = _selectedArticle

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
                _selectedArticle.postValue(article.copy(isFavorite = false))
            } else {
                addToFavorites(article)
                _selectedArticle.postValue(article.copy(isFavorite = true))
            }
        }
    }
}
