package com.example.domain.repository

import com.example.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface FavoriteNewsRepository {
    suspend fun addToFavorites(article: Article)
    suspend fun removeFromFavorites(article: Article)
    suspend fun isArticleFavorite(url: String): Boolean
    fun getFavoriteNews(): Flow<List<Article>>
}