package com.smilehunter.ablebody.sharedPreferences.di

import android.content.Context
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MasterKeyModule {

    @Singleton
    @Provides
    fun provideMasterKey(@ApplicationContext context: Context): MasterKey =
        MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS).run {
            setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            build()
        }
}