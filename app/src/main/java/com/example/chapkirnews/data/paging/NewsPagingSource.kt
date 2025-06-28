package com.example.chapkirnews.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.chapkirnews.data.api.ApiService
import com.example.chapkirnews.data.mapper.toDomain
import com.example.chapkirnews.domain.model.Article

class NewsPagingSource(
    private val api: ApiService,
    private val query: String
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize

            val response = api.getLatestNews(
                query = query,
                page = page,
                pageSize = pageSize
            )

            val articles = response.articles.map { it.toDomain() }

            LoadResult.Page(
                data = articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (articles.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}