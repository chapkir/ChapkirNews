package com.example.chapkirnews.domain.usecase.all_news

import androidx.paging.PagingData
import com.example.chapkirnews.domain.model.Article
import com.example.chapkirnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(query: String): Flow<PagingData<Article>> {
        return repository.getNewsPaging(query).flow
    }
}