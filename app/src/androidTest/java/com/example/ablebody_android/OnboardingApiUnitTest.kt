package com.example.ablebody_android

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class OnboardingApiUnitTest {

    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private val tokenSharedPreferencesRepository = TokenSharedPreferencesRepository(applicationContext = appContext)
    private val networkRepository = NetworkRepository(tokenSharedPreferencesRepository)

    private val authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiODc1NTM4MCIsImV4cCI6MTY4OTE1MTM0N30.o96O1ijkZreWOMHllSHxcxR3xQ6yvlvA7j1XneXORoq_7aquOOwxMTa-ShFeOUdp0PaqiYm2ZIdMTSqXarqJjA"
    private val refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwidWlkIjoiODc1NTM4MCIsImV4cCI6MTY5MDQ0OTg2OH0.j9j-yOX5JNt4ENCA-6A4KuLpNoBK6CiBwLbJwW1nuPVZEUFJYp1DxVs03p3eUg89qG8CJNZ-BgieN4oaWG6MXA"

    @Test
    fun sendSMS() {
        val response = networkRepository.sendSMS(
            phoneNumber = "01026289219",
            isNotTestMessage = true
        )

        println("response: $response, body: ${response.body()}")
        Assert.assertEquals(response.code(), 200)
    }

    @Test
    fun checkSMS() {
        val response = networkRepository.checkSMS(
            phoneConfirmId = 39,
            verifyingCode = "9092"
        )

        println("response: $response, body: ${response.body()}")
        Assert.assertEquals(response.code(), 200)
    }

    @Test
    fun checkNickname() {
        val response = networkRepository.checkNickname("김민준")

        println("response: $response, body: ${response.body()}")
        Assert.assertEquals(response.code(), 200)
    }

    @Test
    fun createNewUser() {
        val response = networkRepository.createNewUser(
            Gender.남자,
            "brother",
            1,
            "2869",
            true,
            agreeMarketingConsent = true
        )
        println("response: $response, body: ${response.body()}")
        Assert.assertEquals(response.code(), 200)
    }

    @Test
    fun refreshToken() {
        val response = networkRepository.getRefreshToken(refreshToken = refreshToken)
        println("response: $response, body: ${response.body()}")
        Assert.assertEquals(response.code(), 200)
    }

    @Test
    fun getUserData() {
        val response = networkRepository.getUserData(authToken)
        println("response: $response, body: ${response.body()}")
        Assert.assertEquals(response.code(), 200)
    }

    @Test
    fun getDummyToken() {
        val response = networkRepository.getDummyToken()

        println("response: $response, body: ${response.body()}")
        Assert.assertEquals(response.code(), 200)
    }

    @Test
    fun updateFCMTokenAndAppVersion() {
        val response = networkRepository.updateFCMTokenAndAppVersion(
            authToken = authToken ,
            fcmToken = "hello", appVersion = "2.0.0"
        )

        println("response: $response, body: ${response.body()}")
        Assert.assertEquals(response.code(), 200)
    }
}