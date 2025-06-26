package com.example.chapkirnews.domain.usecase.favorites_news

import com.example.chapkirnews.domain.model.Article
import com.example.chapkirnews.domain.repository.FavoriteNewsRepository
import javax.inject.Inject

class AddArticleToFavoritesUseCase @Inject constructor(
    private val repository: FavoriteNewsRepository
) {
    suspend operator fun invoke(article: Article) {
        repository.addToFavorites(article)
    }
}