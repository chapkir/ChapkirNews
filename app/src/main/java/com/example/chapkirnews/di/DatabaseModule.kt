package com.example.chapkirnews.di

import android.content.Context
import androidx.room.Room
import com.example.chapkirnews.data.local.FavoriteArticleDao
import com.example.chapkirnews.data.local.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "news_database"
        ).build()
    }

    @Provides
    fun provideFavoriteArticleDao(db: NewsDatabase): FavoriteArticleDao {
        return db.favoriteArticleDao()
    }
}