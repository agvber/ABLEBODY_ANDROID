package com.example.ablebody_android.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ablebody_android.database.model.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SearchHistoryDao {
    @Query("SELECT * FROM search_history")
    abstract fun getAll(): Flow<List<SearchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(searchHistoryEntity: SearchHistoryEntity)

    @Query("DELETE FROM search_history")
    abstract fun deleteAll()
}