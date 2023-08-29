package com.example.ablebody_android

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TokenReloadUnitTest {

    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private val tokenSharedPreferencesRepository = TokenSharedPreferencesRepository(applicationContext = appContext)

    private val authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwidWlkIjoiODc1NTM4MCIsImV4cCI6MTY5NDQ1NjY0NX0.YCnen6MUqbnQRUIjdvIdwp1Bo8P8H6rS02xWgarqIPE8kBDeKjy0vbkvnnaNXwrJPz308zStlKsMQfx68RXiGA"
    private val refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwidWlkIjoiODc1NTM4MCIsImV4cCI6MTY5NDQ1NjY0NX0.YCnen6MUqbnQRUIjdvIdwp1Bo8P8H6rS02xWgarqIPE8kBDeKjy0vbkvnnaNXwrJPz308zStlKsMQfx68RXiGA"


    @Before
    fun putToken() {
        tokenSharedPreferencesRepository.putAuthToken(authToken)
        tokenSharedPreferencesRepository.putRefreshToken(refreshToken)
    }

    @After
    fun checkToken() {
        val authToken = tokenSharedPreferencesRepository.getAuthToken()
        val refreshToken = tokenSharedPreferencesRepository.getRefreshToken()
        Assert.assertEquals(authToken, this.authToken)
        Assert.assertEquals(refreshToken, this.refreshToken)
    }

    @Test
    fun getToken() {
        val authToken = tokenSharedPreferencesRepository.getAuthToken()
        val refreshToken = tokenSharedPreferencesRepository.getRefreshToken()
        println("authToken: $authToken, refreshToken: $refreshToken")
    }
}