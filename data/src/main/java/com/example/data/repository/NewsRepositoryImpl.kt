package com.example.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.data.api.ApiService
import com.example.data.paging.NewsPagingSource
import com.example.domain.model.Article
import com.example.domain.repository.NewsRepository
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