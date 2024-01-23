package com.smilehunter.ablebody.network.di

import com.smilehunter.ablebody.network.NetworkService
import com.smilehunter.ablebody.network.NetworkServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RetrofitModule {

    @Singleton
    @Binds
    fun bindNetworkService(
        networkServiceImpl: NetworkServiceImpl
    ): NetworkService

}