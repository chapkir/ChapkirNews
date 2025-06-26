package com.example.chapkirnews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteArticleEntity::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun favoriteArticleDao(): FavoriteArticleDao
}