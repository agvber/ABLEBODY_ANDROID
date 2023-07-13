package com.example.ablebody_android

import android.content.Context

class TokenSharedPreferencesRepository(applicationContext: Context) {

    private val sharedPreferences = TokenSharedPreferences.getInstance(applicationContext)

    fun getAuthToken(): String? = sharedPreferences.getString("AUTH_TOKEN", null)
    fun getRefreshToken(): String? = sharedPreferences.getString("REFRESH_TOKEN", null)

    fun putAuthToken(token: String) = sharedPreferences.edit().putString("AUTH_TOKEN", token).apply()
    fun putRefreshToken(token: String) = sharedPreferences.edit().putString("REFRESH_TOKEN", token).apply()

}