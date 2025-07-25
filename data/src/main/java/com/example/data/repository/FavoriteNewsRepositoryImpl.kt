package com.example.data.repository

import com.example.data.local.FavoriteArticleDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.domain.model.Article
import com.example.domain.repository.FavoriteNewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteNewsRepositoryImpl @Inject constructor(
    private val dao: FavoriteArticleDao
) : FavoriteNewsRepository {

    override suspend fun addToFavorites(article: Article) {
        dao.insertArticle(article.toEntity())
    }

    override suspend fun removeFromFavorites(article: Article) {
        dao.deleteArticle(article.toEntity())
    }

    override suspend fun isArticleFavorite(url: String): Boolean {
        return dao.isArticleFavorite(url)
    }

    override fun getFavoriteNews(): Flow<List<Article>> {
        return dao.getAllFavorites().map { entityList ->
            entityList.map { it.toDomain() }
        }
    }
}