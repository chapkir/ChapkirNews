package com.example.data.mapper

import com.example.data.local.FavoriteArticleEntity
import com.example.domain.model.Article

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