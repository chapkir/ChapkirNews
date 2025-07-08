package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.FavoriteArticleDao
import com.example.data.local.FavoriteArticleEntity

@Database(entities = [FavoriteArticleEntity::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun favoriteArticleDao(): FavoriteArticleDao
}