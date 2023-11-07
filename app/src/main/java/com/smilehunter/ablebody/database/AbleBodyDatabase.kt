package com.smilehunter.ablebody.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smilehunter.ablebody.database.dao.SearchHistoryDao
import com.smilehunter.ablebody.database.model.SearchHistoryEntity

@Database(
    entities = [
        SearchHistoryEntity::class
    ],
    version = 1
)
abstract class AbleBodyDatabase: RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}