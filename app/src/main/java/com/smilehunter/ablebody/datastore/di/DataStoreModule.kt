package com.smilehunter.ablebody.datastore.di

import com.smilehunter.ablebody.datastore.DataStoreService
import com.smilehunter.ablebody.datastore.DataStoreServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreModule {

    @Singleton
    @Binds
    fun bindsDataStoreService(
        dataStoreServiceImpl: DataStoreServiceImpl
    ): DataStoreService

}