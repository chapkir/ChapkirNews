package com.example.chapkirnews.presentation.screens.newsfeed_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.chapkirnews.domain.model.Article
import com.example.chapkirnews.domain.repository.NewsRepository
import com.example.chapkirnews.domain.usecase.all_news.GetNewsUseCase
import com.example.chapkirnews.domain.usecase.favorites_news.AddArticleToFavoritesUseCase
import com.example.chapkirnews.domain.usecase.favorites_news.GetFavoriteNewsUseCase
import com.example.chapkirnews.domain.usecase.favorites_news.RemoveArticleFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsfeedViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val addToFavorites: AddArticleToFavoritesUseCase,
    private val removeFromFavorites: RemoveArticleFromFavoritesUseCase,
    private val getFavorites: GetFavoriteNewsUseCase
) : ViewModel() {

    private val searchQuery = MutableStateFlow("айти")

    private val _uiState = MutableStateFlow(NewsfeedUiState())
    val uiState: StateFlow<NewsfeedUiState> = _uiState.asStateFlow()

    private val _favorites = MutableStateFlow<List<String>>(emptyList())

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val newsPagingFlow: Flow<PagingData<Article>> = searchQuery
        .debounce(500)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            newsRepository.getNewsPaging(query).flow
                .cachedIn(viewModelScope)
        }
        .combine(_favorites) { pagingData, favorites ->
            pagingData.map { article ->
                article.copy(isFavorite = article.url in favorites)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    init {
        observeFavorites()
    }

    fun openSearch() {
        _uiState.update { it.copy(isSearchActive = true) }
    }

    fun closeSearch() {
        _uiState.update { it.copy(isSearchActive = false, searchQuery = "") }
        searchQuery.value = ""
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchQuery.value = query
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
            getFavorites().collectLatest { favoriteArticles ->
                _favorites.value = favoriteArticles.map { it.url }
            }
        }
    }
}
