package com.example.ablebody_android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ablebody_android.database.dao.SearchHistoryDao
import com.example.ablebody_android.database.model.SearchHistoryEntity

@Database(
    entities = [
        SearchHistoryEntity::class
    ],
    version = 1
)
abstract class AbleBodyDatabase: RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}