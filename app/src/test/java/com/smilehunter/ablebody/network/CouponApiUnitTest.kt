package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.network.utils.TestRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CouponApiUnitTest {
    private val networkService: NetworkService = TestRetrofit.getInstance()

    @Test
    fun getCouponBags() {
        val response = runBlocking {
            networkService.getCouponBags()
        }
        println(response)
    }
}