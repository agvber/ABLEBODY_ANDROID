package com.smilehunter.ablebody.data.di

import com.smilehunter.ablebody.data.repository.BookmarkRepository
import com.smilehunter.ablebody.data.repository.BookmarkRepositoryImpl
import com.smilehunter.ablebody.data.repository.BrandRepository
import com.smilehunter.ablebody.data.repository.BrandRepositoryImpl
import com.smilehunter.ablebody.data.repository.FindCodyRepository
import com.smilehunter.ablebody.data.repository.FindCodyRepositoryImpl
import com.smilehunter.ablebody.data.repository.FindItemRepository
import com.smilehunter.ablebody.data.repository.FindItemRepositoryImpl
import com.smilehunter.ablebody.data.repository.NotificationRepository
import com.smilehunter.ablebody.data.repository.NotificationRepositoryImpl
import com.smilehunter.ablebody.data.repository.OnboardingRepository
import com.smilehunter.ablebody.data.repository.OnboardingRepositoryImpl
import com.smilehunter.ablebody.data.repository.SearchRepository
import com.smilehunter.ablebody.data.repository.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsOnboardingRepository(
        onboardingRepositoryImpl: OnboardingRepositoryImpl
    ): OnboardingRepository

    @Binds
    fun bindsBrandRepository(
        brandRepositoryImpl: BrandRepositoryImpl
    ): BrandRepository

    @Binds
    fun bindsBookmarkRepository(
        bookmarkRepositoryImpl: BookmarkRepositoryImpl
    ): BookmarkRepository

    @Binds
    fun bindsFindItemRepository(
        findItemRepositoryImpl: FindItemRepositoryImpl
    ): FindItemRepository

    @Binds
    fun bindsFindCodyRepository(
        findCodyRepositoryImpl: FindCodyRepositoryImpl
    ): FindCodyRepository

    @Binds
    fun bindsSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    fun bindsNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository
}