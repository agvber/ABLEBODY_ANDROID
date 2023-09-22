package com.example.ablebody_android.data.di

import com.example.ablebody_android.data.repository.BookmarkRepository
import com.example.ablebody_android.data.repository.BookmarkRepositoryImpl
import com.example.ablebody_android.data.repository.BrandRepository
import com.example.ablebody_android.data.repository.BrandRepositoryImpl
import com.example.ablebody_android.data.repository.FindCodyRepository
import com.example.ablebody_android.data.repository.FindCodyRepositoryImpl
import com.example.ablebody_android.data.repository.FindItemRepository
import com.example.ablebody_android.data.repository.FindItemRepositoryImpl
import com.example.ablebody_android.data.repository.OnboardingRepository
import com.example.ablebody_android.data.repository.OnboardingRepositoryImpl
import com.example.ablebody_android.data.repository.SearchRepository
import com.example.ablebody_android.data.repository.SearchRepositoryImpl
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

    @Binds
    fun bindFindItemRepository(
        findItemRepositoryImpl: FindItemRepositoryImpl
    ): FindItemRepository

    @Binds
    fun bindFindCodyRepository(
        findCodyRepositoryImpl: FindCodyRepositoryImpl
    ): FindCodyRepository

    @Binds
    fun bindsSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository
}