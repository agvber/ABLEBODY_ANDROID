package com.smilehunter.ablebody.database.di

import com.smilehunter.ablebody.database.AbleBodyDatabase
import com.smilehunter.ablebody.database.dao.SearchHistoryDao
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