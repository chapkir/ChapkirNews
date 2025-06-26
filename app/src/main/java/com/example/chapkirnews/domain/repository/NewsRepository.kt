package com.example.chapkirnews.domain.repository

import com.example.chapkirnews.domain.model.Article

interface NewsRepository {
    suspend fun getNews(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Article>
}