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
}