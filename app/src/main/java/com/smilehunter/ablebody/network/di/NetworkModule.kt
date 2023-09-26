package com.smilehunter.ablebody.network.di

import com.smilehunter.ablebody.network.NetworkService
import com.smilehunter.ablebody.sharedPreferences.TokenSharedPreferences
import com.smilehunter.ablebody.network.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkhttpClient(
        tokenSharedPreferences: Provider<TokenSharedPreferences>,
        networkService: Provider<NetworkService>
    ): OkHttpClient = OkHttpClient.Builder().run {
        addInterceptor {
            with(it) {
                val newRequest = request().newBuilder().run {
                    addHeader("Authorization", "Bearer ${tokenSharedPreferences.get().getAuthToken()}")
                    build()
                }
                return@with proceed(newRequest)
            }
        }
        authenticator(TokenAuthenticator(tokenSharedPreferences, networkService))
        build()
    }
}