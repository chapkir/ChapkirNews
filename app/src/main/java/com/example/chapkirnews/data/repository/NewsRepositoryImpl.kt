package com.example.chapkirnews.data.repository

import com.example.chapkirnews.data.api.ApiService
import com.example.chapkirnews.data.mapper.toDomain
import com.example.chapkirnews.domain.model.Article
import com.example.chapkirnews.domain.repository.NewsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val api: ApiService
) : NewsRepository {

    override suspend fun getNews(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Article> {
        val response = api.getLatestNews(
            query = query,
            page = page,
            pageSize = pageSize
        )

        return response.articles.map { it.toDomain() }
    }
}