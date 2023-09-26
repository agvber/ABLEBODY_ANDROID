package com.example.ablebody_android.network.di

import com.example.ablebody_android.network.NetworkService
import com.example.ablebody_android.network.NetworkServiceImpl
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