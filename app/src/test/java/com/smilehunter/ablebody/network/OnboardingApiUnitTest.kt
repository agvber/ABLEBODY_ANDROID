package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.network.utils.TestRetrofit
import com.smilehunter.ablebody.utils.printResponse
import kotlinx.coroutines.runBlocking
import org.junit.Test

class OnboardingApiUnitTest {
    private val networkService: NetworkService = TestRetrofit.getInstance()

    @Test
    fun sendSMS() {
        val response = runBlocking {
            networkService.sendSMS(
                phoneNumber = "01026289219",
                isNotTestMessage = true
            )
        }
        printResponse(response)
    }

    @Test
    fun checkSMS() {
        val response = runBlocking {
            networkService.checkSMS(
                phoneConfirmId = 101,
                verifyingCode = "3373"
            )
        }
        printResponse(response)
    }

    @Test
    fun checkNickname() {
        val response = runBlocking {
            networkService.checkNickname("김민준")
        }
        printResponse(response)
    }

    @Test
    fun createNewUser() {
        val response = runBlocking {
            networkService.createNewUser(
                com.smilehunter.ablebody.data.dto.Gender.MALE,
                "brother",
                1,
                "2869",
                true,
                agreeMarketingConsent = true
            )
        }
        printResponse(response)
    }

    @Test
    fun refreshToken() {
        val response = runBlocking {
            networkService.getRefreshToken(refreshToken = "refreshToken")
        }
        printResponse(response)
    }

    @Test
    fun getUserData() {
        val response = runBlocking { networkService.getUserData() }
        printResponse(response)
    }

    @Test
    fun getDummyToken() {
        val response = runBlocking { networkService.getDummyToken() }
        printResponse(response)
    }

    @Test
    fun updateFCMTokenAndAppVersion() {
        val response = runBlocking {
            networkService.updateFCMTokenAndAppVersion(
                fcmToken = "hello", appVersion = "2.0.0"
            )
        }
        printResponse(response)
    }
}