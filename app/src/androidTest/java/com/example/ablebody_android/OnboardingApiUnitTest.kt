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

    private val authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiNjYzNDUxNCIsImV4cCI6MTY5MjY5MjE1NX0.Y4J7fM5gtLzRjdeCsggdJj7tkOuL5Hjgtx0Tb3JDzVkUAtN9uyAD_LKodyKJ56tJQOnad8jYNGuR_lx9bdwS-Q"
    private val refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwidWlkIjoiNjYzNDUxNCIsImV4cCI6MTY5Mzg5OTk1NX0.CcrV-6-Op2ETWlGyP6GzoSizDN1YDBFIcKWSoZ6KagcfKtRNfSra3T1ZfQ5VX_tROj8eXsZ7T1kn1GoIUkaVOQ"

    @Test
    fun sendSMS() {
        val response = networkRepository.sendSMS(
            phoneNumber = "01092393487",
            isNotTestMessage = true
        )

        println("response: $response, body: ${response.body()}")
        Assert.assertEquals(response.code(), 200)
    }

    @Test
    fun checkSMS() {
        val response = networkRepository.checkSMS(
            phoneConfirmId = 98,
            verifyingCode = "6063"
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
            Gender.MALE,
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
