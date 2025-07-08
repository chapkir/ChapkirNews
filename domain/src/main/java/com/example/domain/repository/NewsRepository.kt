package com.example.domain.repository

import androidx.paging.Pager
import com.example.domain.model.Article

interface NewsRepository {
    fun getNewsPaging(query: String): Pager<Int, Article>
}