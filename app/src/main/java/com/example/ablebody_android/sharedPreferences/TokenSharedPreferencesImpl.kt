package com.example.ablebody_android.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val FILE_NAME: String = "JWT_TOKEN"

@Singleton
class TokenSharedPreferencesImpl @Inject constructor(
    masterKey: MasterKey,
    @ApplicationContext context: Context
): TokenSharedPreferences {
    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _registerOnClearedListener = MutableSharedFlow<Unit>()
    override val registerOnClearedListener: Flow<Unit>
        get() = _registerOnClearedListener

    override fun getAuthToken(): String? =
        sharedPreferences.getString("AUTH_TOKEN", null)

    override fun getRefreshToken(): String? =
        sharedPreferences.getString("REFRESH_TOKEN", null)

    override fun putAuthToken(token: String) =
        sharedPreferences.edit().putString("AUTH_TOKEN", token).apply()

    override fun putRefreshToken(token: String) =
        sharedPreferences.edit().putString("REFRESH_TOKEN", token).apply()

    override suspend fun clear() {
        sharedPreferences.edit().clear().apply()
        _registerOnClearedListener.emit(Unit)
    }
}