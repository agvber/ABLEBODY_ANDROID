package com.smilehunter.ablebody.sharedPreferences.di

import com.smilehunter.ablebody.sharedPreferences.TokenSharedPreferences
import com.smilehunter.ablebody.sharedPreferences.TokenSharedPreferencesImpl
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