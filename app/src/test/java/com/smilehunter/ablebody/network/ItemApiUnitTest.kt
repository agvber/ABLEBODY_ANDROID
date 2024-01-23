package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.network.utils.TestRetrofit
import com.smilehunter.ablebody.utils.printResponse
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ItemApiUnitTest {
    private val networkService: NetworkService = TestRetrofit.getInstance()

    @Test
    fun itemDetailTest(){
        val response = runBlocking {networkService.itemDetail(1)}
        println(response)
    }
}