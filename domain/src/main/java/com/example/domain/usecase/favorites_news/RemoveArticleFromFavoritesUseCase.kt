package com.example.domain.usecase.favorites_news

import com.example.domain.model.Article
import com.example.domain.repository.FavoriteNewsRepository
import javax.inject.Inject

class RemoveArticleFromFavoritesUseCase @Inject constructor(
    private val repository: FavoriteNewsRepository
) {
    suspend operator fun invoke(article: Article) {
        repository.removeFromFavorites(article)
    }
}