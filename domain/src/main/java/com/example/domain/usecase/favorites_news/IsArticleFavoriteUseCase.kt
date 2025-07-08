package com.example.domain.usecase.favorites_news

import com.example.domain.repository.FavoriteNewsRepository
import javax.inject.Inject

class IsArticleFavoriteUseCase @Inject constructor(
    private val repository: FavoriteNewsRepository
) {
    suspend operator fun invoke(url: String): Boolean {
        return repository.isArticleFavorite(url)
    }
}