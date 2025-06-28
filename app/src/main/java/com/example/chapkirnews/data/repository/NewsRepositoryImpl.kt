package com.example.chapkirnews.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.chapkirnews.data.api.ApiService
import com.example.chapkirnews.data.mapper.toDomain
import com.example.chapkirnews.data.paging.NewsPagingSource
import com.example.chapkirnews.domain.model.Article
import com.example.chapkirnews.domain.repository.NewsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val api: ApiService
) : NewsRepository {

    override fun getNewsPaging(query: String): Pager<Int, Article> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { NewsPagingSource(api, query) }
        )
    }
}