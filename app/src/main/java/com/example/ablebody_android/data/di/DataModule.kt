package com.example.ablebody_android.data.di

import com.example.ablebody_android.data.repository.BookmarkRepository
import com.example.ablebody_android.data.repository.BookmarkRepositoryImpl
import com.example.ablebody_android.data.repository.BrandRepository
import com.example.ablebody_android.data.repository.BrandRepositoryImpl
import com.example.ablebody_android.data.repository.OnboardingRepository
import com.example.ablebody_android.data.repository.OnboardingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindOnboardingRepository(
        onboardingRepositoryImpl: OnboardingRepositoryImpl
    ): OnboardingRepository

    @Binds
    fun bindBrandRepository(
        brandRepositoryImpl: BrandRepositoryImpl
    ): BrandRepository

    @Binds
    fun bindBookmarkRepository(
        bookmarkRepositoryImpl: BookmarkRepositoryImpl
    ): BookmarkRepository
}