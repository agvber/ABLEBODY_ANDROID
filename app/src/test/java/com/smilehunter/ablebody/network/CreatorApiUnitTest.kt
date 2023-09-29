package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.data.dto.NetworkLikedLocations
import com.smilehunter.ablebody.network.utils.TestRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CreatorApiUnitTest {
    private val networkAPI: NetworkService = TestRetrofit.getInstance()

    @Test
    fun creatorDetailLikeUsers() {
        val response = runBlocking {
            networkAPI.creatorDetailLikeUsers(
                where = NetworkLikedLocations.BOARD,
                id = 2
            )
        }
        println(response)
    }
}