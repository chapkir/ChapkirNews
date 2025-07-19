package com.example.app_xml.presentation.newsfeed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.domain.model.Article
import com.example.domain.repository.NewsRepository
import com.example.domain.usecase.favorites_news.AddArticleToFavoritesUseCase
import com.example.domain.usecase.favorites_news.GetFavoriteNewsUseCase
import com.example.domain.usecase.favorites_news.RemoveArticleFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsfeedViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val addToFavorites: AddArticleToFavoritesUseCase,
    private val removeFromFavorites: RemoveArticleFromFavoritesUseCase,
    private val getFavorites: GetFavoriteNewsUseCase
) : ViewModel() {

    private val _articles = MutableLiveData<PagingData<Article>>()
    val articles: LiveData<PagingData<Article>> = _articles

    private val _favorites = MutableLiveData<List<String>>(emptyList())

    private var currentQuery: String = ""

    init {
        observeFavorites()
        loadNews()
    }

    private fun loadNews() {
        viewModelScope.launch {
            newsRepository.getNewsPaging(currentQuery.ifBlank { "apple" }).flow
                .cachedIn(viewModelScope)
                .combine(_favorites.asFlow()) { pagingData, favorites ->
                    pagingData.map { article ->
                        article.copy(isFavorite = article.url in favorites)
                    }
                }
                .collectLatest { pagingData ->
                    _articles.postValue(pagingData)
                }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavorites().collectLatest { favorites ->
                _favorites.postValue(favorites.map { it.url })
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

    fun onSearchQueryChange(query: String) {
        currentQuery = query
        loadNews()
    }
}