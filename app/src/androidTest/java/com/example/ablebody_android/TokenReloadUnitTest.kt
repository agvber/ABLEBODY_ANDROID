package com.example.ablebody_android

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.AssertionError

@RunWith(AndroidJUnit4::class)
class TokenReloadUnitTest {

    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private val tokenSharedPreferencesRepository = TokenSharedPreferencesRepository(applicationContext = appContext)
    private val networkRepository = NetworkRepository(tokenSharedPreferencesRepository)

    private val authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiODc1NTM4MCIsImV4cCI6MTY4OTIzMzYxOX0.iiEuwqPrfldSUvOVmIs4vgZ-fKEJFL-L8raicrfemEZNCRS79t8U5PpWDz4xQC9J0H-tVPkSXfDTXZawTW9Sug"
    private val refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwidWlkIjoiODc1NTM4MCIsImV4cCI6MTY5MDQ0MzkwNX0.lc0qNYA6s6a66m6iLL93o9V6k_7FjnyD9WU4_GCMBraenVejUpOt0n93C2qg4fKQYikTM-RsQku_IroHa3UjNg"


    @Before
    fun putToken() {
        tokenSharedPreferencesRepository.putAuthToken(authToken)
        tokenSharedPreferencesRepository.putRefreshToken(refreshToken)
    }

    @Test
    fun getUserData() {
        val authToken = tokenSharedPreferencesRepository.getAuthToken()!!
        val response = networkRepository.getUserData(authToken)
        println("response: $response\nbody: ${response.body()}")
    }
}