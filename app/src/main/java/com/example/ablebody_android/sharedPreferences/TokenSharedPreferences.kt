package com.example.ablebody_android.sharedPreferences

import kotlinx.coroutines.flow.Flow


interface TokenSharedPreferences {
    val registerOnClearedListener: Flow<Unit>
    fun getAuthToken(): String?
    fun getRefreshToken(): String?

    fun putAuthToken(token: String)
    fun putRefreshToken(token: String)
    suspend fun clear()
}