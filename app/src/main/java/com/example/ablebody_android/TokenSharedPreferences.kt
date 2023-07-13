package com.example.ablebody_android

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object TokenSharedPreferences {
    private const val FILE_NAME: String = "JWT_TOKEN"

    private var sharedPreferences: SharedPreferences? = null

    fun getInstance(applicationContext: Context): SharedPreferences {

        if (sharedPreferences == null) {
            sharedPreferences = createEncryptedSharedPreferences(applicationContext)
        }
        return sharedPreferences!!
    }


    private fun buildMasterKey(applicationContext: Context) = MasterKey
        .Builder(applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private fun createEncryptedSharedPreferences(
        applicationContext: Context
    ): SharedPreferences {
        return EncryptedSharedPreferences.create(
            applicationContext,
            FILE_NAME,
            buildMasterKey(applicationContext),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}