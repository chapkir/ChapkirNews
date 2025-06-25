package com.example.chapkirnews.data.mapper

import com.example.chapkirnews.data.model.ArticleDto
import com.example.chapkirnews.domain.model.Article

fun ArticleDto.toDomain(): Article {
    return Article(
        sourceName = this.source?.name,
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        imageUrl = this.urlToImage,
        publishedAt = this.publishedAt,
        content = this.content
    )
}