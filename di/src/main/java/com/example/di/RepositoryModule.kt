package com.example.di

import com.example.data.api.ApiService
import com.example.data.local.FavoriteArticleDao
import com.example.data.repository.FavoriteNewsRepositoryImpl
import com.example.data.repository.NewsRepositoryImpl
import com.example.domain.repository.FavoriteNewsRepository
import com.example.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNewsRepository(
        api: ApiService
    ): NewsRepository = NewsRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideFavoriteNewsRepository(
        dao: FavoriteArticleDao
    ): FavoriteNewsRepository = FavoriteNewsRepositoryImpl(dao)

}