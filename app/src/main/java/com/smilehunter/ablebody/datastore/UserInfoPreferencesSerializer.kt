package com.smilehunter.ablebody.datastore

import androidx.datastore.core.Serializer
import com.smilehunter.ablebody.UserInfoPreferences
import java.io.InputStream
import java.io.OutputStream

class UserInfoPreferencesSerializer: Serializer<UserInfoPreferences> {
    override val defaultValue: UserInfoPreferences
        get() = UserInfoPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserInfoPreferences = UserInfoPreferences.parseFrom(input)

    override suspend fun writeTo(t: UserInfoPreferences, output: OutputStream) = t.writeTo(output)
}