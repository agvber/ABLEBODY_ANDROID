package com.example.ablebody_android.database.di

import com.example.ablebody_android.database.AbleBodyDatabase
import com.example.ablebody_android.database.dao.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun providesSearchHistoryDao(
        database: AbleBodyDatabase
    ): SearchHistoryDao = database.searchHistoryDao()

}