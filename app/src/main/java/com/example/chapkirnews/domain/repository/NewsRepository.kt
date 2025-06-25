package com.example.chapkirnews.domain.repository

import com.example.chapkirnews.domain.model.Article

interface NewsRepository {
    suspend fun getLatestNews(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Article>
}