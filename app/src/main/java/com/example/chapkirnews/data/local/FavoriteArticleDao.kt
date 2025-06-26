package com.example.chapkirnews.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteArticleDao {

    @Query("SELECT * FROM favorite_articles")
    fun getAllFavorites(): Flow<List<FavoriteArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: FavoriteArticleEntity)

    @Delete
    suspend fun deleteArticle(article: FavoriteArticleEntity)

    @Query("SELECT EXISTS(SELECT * FROM favorite_articles WHERE url = :url)")
    suspend fun isArticleFavorite(url: String): Boolean
}