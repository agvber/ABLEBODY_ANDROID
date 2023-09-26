package com.example.ablebody_android.network

import com.example.ablebody_android.network.utils.TestRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Test

class NotificationApiUnitTest {
    private val networkAPI: NetworkService = TestRetrofit.getInstance()
    @Test
    fun getMyNoti() {
        val response = runBlocking { networkAPI.getMyNoti() }
        println(response.toString())
    }

   @Test
    fun checkMyNoti() {
        val response = runBlocking { networkAPI.checkMyNoti(224512) }
        println(response.toString())
    }
}