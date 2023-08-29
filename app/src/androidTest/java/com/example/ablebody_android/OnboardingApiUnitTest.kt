package com.example.ablebody_android

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class OnboardingApiUnitTest {

    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private val tokenSharedPreferencesRepository = TokenSharedPreferencesRepository(applicationContext = appContext)
    private val networkRepository = NetworkRepository(tokenSharedPreferencesRepository)

    private val authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiODc1NTM4MCIsImV4cCI6MTY5MzI4MTk0M30.9OFaeQm86-jsBTk61gZA2uyaiwMKpGm2Mm7-RZDsI2_81b5WTMgApbdbvorQyHkfJrh-Ze2C4Rd9Kd9BaZtzwA"
    private val refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwidWlkIjoiODc1NTM4MCIsImV4cCI6MTY5NDQ4OTc0M30.O1LsBZbx3fh1tjhAz7HOZsKRSiLzYw_YDjcEA07AVMqXoAMCOQp519rcxKYe9PA5IKD8V0olfMeqK8pxbGFtXQ"

    @Before
    fun putToken() {
        tokenSharedPreferencesRepository.putAuthToken(authToken)
        tokenSharedPreferencesRepository.putRefreshToken(refreshToken)
    }

    @After
    fun checkToken() {
        val authToken = tokenSharedPreferencesRepository.getAuthToken()
        val refreshToken = tokenSharedPreferencesRepository.getRefreshToken()

        if (this.authToken != authToken) {
            Log.w("OnboardingUnitTest", "토큰이 변경되었습니다")
            Log.w("authToken", authToken.toString())
            Log.w("refreshToken", refreshToken.toString())
        }
    }

    @Test
    fun sendSMS() {
        val response = networkRepository.sendSMS(
            phoneNumber = "01026289219",
            isNotTestMessage = true
        )
        Log.d("response", response.toString())
        Log.d("body", response.body().toString())
        Assert.assertEquals(response.code(), 200)
    }

    @Test
    fun checkSMS() {
        val response = networkRepository.checkSMS(
            phoneConfirmId = 101,
            verifyingCode = "3373"
        )
        Log.d("response", response.toString())
        Log.d("body", response.body().toString())
        Assert.assertEquals(response.code(), 200)
    }

    @Test
    fun checkNickname() {
        val response = networkRepository.checkNickname("김민준")
        Log.d("response", response.toString())
        Log.d("body", response.body().toString())
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
        Log.d("response", response.toString())
        Log.d("body", response.body().toString())
        Assert.assertEquals(response.code(), 200)
    }

    @Test
    fun refreshToken() {
        val response = networkRepository.getRefreshToken(refreshToken = refreshToken)
        Log.d("response", response.toString())
        Log.d("body", response.body().toString())
        Assert.assertEquals(response.code(), 200)
    }

    @Test
    fun getUserData() {
        val response = networkRepository.getUserData()
        Log.d("response", response.toString())
        Log.d("body", response.body().toString())
        Assert.assertEquals(response.code(), 200)
    }

    @Test
    fun getDummyToken() {
        val response = networkRepository.getDummyToken()
        Log.d("response", response.toString())
        Log.d("body", response.body().toString())
        Assert.assertEquals(response.code(), 200)
    }

    @Test
    fun updateFCMTokenAndAppVersion() {
        val response = networkRepository.updateFCMTokenAndAppVersion(
            fcmToken = "hello", appVersion = "2.0.0"
        )
        Log.d("response", response.toString())
        Log.d("body", response.body().toString())
        Assert.assertEquals(response.code(), 200)
    }
}
