package com.example.ablebody_android

import android.app.Application

class AbleBodyApplication: Application() {

    lateinit var tokenSharedPreferencesRepository: TokenSharedPreferencesRepository
    lateinit var networkRepository: NetworkRepository

    override fun onCreate() {
        super.onCreate()

        tokenSharedPreferencesRepository = TokenSharedPreferencesRepository(this)
        networkRepository = NetworkRepository(tokenSharedPreferencesRepository)

        tokenSharedPreferencesRepository.putAuthToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9" +
                ".eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiOTk5OTk5OSIsImV4cCI6MTc3OTkzNjE" +
                "0M30.Ewo_tMdZIksV-Y3F3jPNdeuA_4Z5N-yNTwZtF9qyIu6DC03Cga9bw6Zp7k1K2ESwmPHkxF7rWCisyp1LDYMONQ")
    }
}