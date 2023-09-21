package com.example.ablebody_android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "search_history"
)
data class SearchHistoryEntity(
    @PrimaryKey val query: String,
    @ColumnInfo(name = "queried_time") val queriedTime: Long,
)