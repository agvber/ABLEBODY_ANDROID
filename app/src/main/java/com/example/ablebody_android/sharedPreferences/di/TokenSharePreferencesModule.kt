package com.example.ablebody_android.sharedPreferences.di

import com.example.ablebody_android.sharedPreferences.TokenSharedPreferences
import com.example.ablebody_android.sharedPreferences.TokenSharedPreferencesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface TokenSharePreferencesModule {

    @Binds
    fun bindTokenSharePreferences(
        tokenSharedPreferencesImpl: TokenSharedPreferencesImpl
    ): TokenSharedPreferences

}