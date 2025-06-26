package com.example.chapkirnews.data.mapper

import com.example.chapkirnews.data.local.FavoriteArticleEntity
import com.example.chapkirnews.domain.model.Article

fun Article.toEntity(): FavoriteArticleEntity = FavoriteArticleEntity(
    url = url,
    sourceName = sourceName,
    author = author,
    title = title,
    description = description,
    imageUrl = imageUrl,
    publishedAt = publishedAt,
    content = content
)

fun FavoriteArticleEntity.toDomain(): Article = Article(
    url = url,
    sourceName = sourceName,
    author = author,
    title = title,
    description = description,
    imageUrl = imageUrl,
    publishedAt = publishedAt,
    content = content
)