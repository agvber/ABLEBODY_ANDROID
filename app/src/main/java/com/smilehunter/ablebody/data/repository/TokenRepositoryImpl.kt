package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.BuildConfig
import com.smilehunter.ablebody.sharedPreferences.TokenSharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenSharedPreferences: TokenSharedPreferences
): TokenRepository {

    init {
        if (BuildConfig.DEBUG) {
            tokenSharedPreferences.putAuthToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiOTk5OTk5OSIsImV4cCI6MTc3OTkzNjE0M30.Ewo_tMdZIksV-Y3F3jPNdeuA_4Z5N-yNTwZtF9qyIu6DC03Cga9bw6Zp7k1K2ESwmPHkxF7rWCisyp1LDYMONQ")
        }
    }

    override val hasToken: Boolean
        get() = tokenSharedPreferences.getAuthToken().let { !it.isNullOrBlank() }

    override val registerOnClearedListener: Flow<Unit>
        get() = tokenSharedPreferences.registerOnClearedListener
            .distinctUntilChanged()
            .onStart { if (!hasToken) emit(Unit) }
}