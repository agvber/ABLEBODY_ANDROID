package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.network.utils.TestRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Test

class UserApiUnitTest {
    private val networkService: NetworkService = TestRetrofit.getInstance()

    @Test
    fun getMyUserData() {
        val response = runBlocking { networkService.getMyUserData() }
        println(response)
    }

    @Test
    fun getUserData() {
        val response = runBlocking { networkService.getUserData(uid = "5920702") }
        println(response)
    }

    @Test
    fun getMyBoard() {
        val response = runBlocking { networkService.getMyBoard() }
        println(response)
    }

    @Test
    fun getUserAdConsent() {
        val response = runBlocking { networkService.getUserAdConsent() }
        println(response)
    }

    @Test
    fun acceptUserAdConsent() {
        val response = runBlocking { networkService.acceptUserAdConsent() }
        println(response)
    }

    @Test
    fun suggestion() {
        val response = runBlocking { networkService.suggestion("애블바디가 성장하는 그날까지!") }
        println(response)
    }
}