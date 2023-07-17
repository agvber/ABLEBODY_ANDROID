package com.example.ablebody_android

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TokenSharedPreferencesUnitTest {

    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private val repository = TokenSharedPreferencesRepository(applicationContext = appContext)

    private val authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiODc1NTM4MCIsImV4cCI6MTY4OTE1MTM0N30.o96O1ijkZreWOMHllSHxcxR3xQ6yvlvA7j1XneXORoq_7aquOOwxMTa-ShFeOUdp0PaqiYm2ZIdMTSqXarqJjA"
    private val refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwidWlkIjoiODc1NTM4MCIsImV4cCI6MTY5MDM1OTE0N30.bxdYxJpNSPl1tk-n-MX27wRRGHO7L6JKwGZsxJHLKrTl2B5u64r8TD8M2tiOKj-PXLVZEBBKMBIPTA1bpdA_nA"


    @Test
    fun checkAuthToken() {
        repository.putAuthToken(token = authToken)

        val result = repository.getAuthToken()
        Assert.assertEquals(result, authToken)
    }

    @Test
    fun checkRefreshToken() {
        repository.putRefreshToken(token = refreshToken)

        val result = repository.getRefreshToken()
        Assert.assertEquals(result, refreshToken)
    }
}