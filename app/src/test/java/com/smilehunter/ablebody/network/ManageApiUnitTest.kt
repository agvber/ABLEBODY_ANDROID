package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.data.dto.request.ReportRequest
import com.smilehunter.ablebody.network.utils.TestRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class ManageApiUnitTest {
    private val networkService: NetworkService = TestRetrofit.getInstance()

    @Test
    fun report() {
        val response = runBlocking {
            val requestBody = ReportRequest(
                contentType = ReportRequest.ContentType.HomeBoard,
                id = 12,
                reason = "재휘님 회의 미루지 말아요",
                content = "written by 김나희"
            )
            networkService.report(requestBody)
        }
        println(response)
        Assert.assertTrue(response.success)
    }
}