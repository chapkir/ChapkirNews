package com.example.chapkirnews.domain.model

data class Article(
    val id: String? = null,
    val sourceName: String?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val imageUrl: String?,
    val publishedAt: String?,
    val content: String?
)