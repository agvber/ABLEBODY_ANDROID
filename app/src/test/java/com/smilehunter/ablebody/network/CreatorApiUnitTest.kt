package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.network.utils.TestRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CreatorApiUnitTest {
    private val networkAPI: NetworkService = TestRetrofit.getInstance()

    @Test
    fun creatorDetailLikeUsersBoard() {
        val response = runBlocking {
            networkAPI.creatorDetailLikeUsersBoard(id = 2)
        }
        println(response)
    }

    @Test
    fun creatorDetailLikeUsersComment() {
        val response = runBlocking {
            networkAPI.creatorDetailLikeUsersComment(id = 2)
        }
        println(response)
    }

    @Test
    fun creatorDetailLikeUsersReply() {
        val response = runBlocking {
            networkAPI.creatorDetailLikeUsersReply(id = 2)
        }
        println(response)
    }

    @Test
    fun creatorDetailComment() {
        val response = runBlocking {
            networkAPI.creatorDetailComment(
                id = 2,
                content = "안녕하세요"
            )
        }
        println(response)
    }

    @Test
    fun creatorDetailReply() {
        val response = runBlocking {
            networkAPI.creatorDetailReply(
                id = 7,
                content = "안녕하세요에 대한 답글"
            )
        }
        println(response)
    }
}