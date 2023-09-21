package com.example.ablebody_android.database.di

import android.content.Context
import androidx.room.Room
import com.example.ablebody_android.database.AbleBodyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AbleBodyDatabase =
        Room.databaseBuilder(
            context,
            AbleBodyDatabase::class.java, "ablebody-database"
        ).build()

}