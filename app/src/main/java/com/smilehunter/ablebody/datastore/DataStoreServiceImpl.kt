package com.smilehunter.ablebody.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.smilehunter.ablebody.UserInfoPreferences
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(AbleBodyDispatcher.IO) ioDispatcher: CoroutineDispatcher
) : DataStoreService {

    private val Context.userInfoDataStore: DataStore<UserInfoPreferences> by dataStore(
        fileName = "user_info_preferences.pb",
        serializer = UserInfoPreferencesSerializer(),
        scope = CoroutineScope(ioDispatcher)
    )

    override fun getUserInfoData() = context.userInfoDataStore.data

    override suspend fun updateUserInfoData(userInfo: (UserInfoPreferences) -> UserInfoPreferences) {
        context.userInfoDataStore.updateData { userInfo(it) }
    }

}