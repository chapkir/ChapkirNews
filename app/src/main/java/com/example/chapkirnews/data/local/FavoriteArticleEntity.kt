package com.example.chapkirnews.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_articles")
data class FavoriteArticleEntity(
    @PrimaryKey val url: String,
    val sourceName: String,
    val author: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val publishedAt: String,
    val content: String
)