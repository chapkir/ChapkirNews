package com.example.chapkirnews.domain.usecase

import com.example.chapkirnews.domain.model.Article
import com.example.chapkirnews.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Article> {
        return repository.getNews(query, page, pageSize)
    }
}