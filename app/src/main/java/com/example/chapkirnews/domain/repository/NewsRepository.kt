package com.example.chapkirnews.domain.repository

import androidx.paging.Pager
import com.example.chapkirnews.domain.model.Article

interface NewsRepository {
    fun getNewsPaging(query: String): Pager<Int, Article>
}