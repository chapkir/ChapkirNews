package com.example.domain.usecase.favorites_news

import com.example.domain.model.Article
import com.example.domain.repository.FavoriteNewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteNewsUseCase @Inject constructor(
    private val repository: FavoriteNewsRepository
) {
    operator fun invoke(): Flow<List<Article>> {
        return repository.getFavoriteNews()
    }
}