package com.example.domain.model

data class Article(
    val sourceName: String = "Неизвестный источник",
    val author: String = "Неизвестный автор",
    val title: String = "Без названия",
    val description: String = "Без описания",
    val url: String = "",
    val imageUrl: String = "",
    val publishedAt: String = "Дата не указана",
    val content: String = "",
    val isFavorite: Boolean = false
)