package com.example.chapkirnews.di

import com.example.chapkirnews.data.api.ApiService
import com.example.chapkirnews.data.repository.NewsRepositoryImpl
import com.example.chapkirnews.domain.repository.NewsRepository
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

}