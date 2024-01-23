package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.network.utils.TestRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AddressApiUnitTest {
    private val networkService: NetworkService = TestRetrofit.getInstance()

    @Test
    fun addAddress() {
        val response = runBlocking {
            networkService.addAddress(
                receiverName = "이재휘",
                phoneNum = "01012345678",
                addressInfo = "경기도 용인시 처인구 낙은로 11",
                detailAddress = "103-404",
                zipCode = "17046",
                deliveryRequest = "문 앞에 놔주세요."
            )
        }
        println(response)
    }

    @Test
    fun getAddress() {
        val response = runBlocking {
            networkService.getAddress()
        }
        println(response)
    }

    @Test
    fun editAddress() {
        val response = runBlocking {
            networkService.editAddress(
                receiverName = "이재휘",
                phoneNum = "01012345678",
                addressInfo = "경기도 용인시 처인구 낙은로 11",
                detailAddress = "103-404",
                zipCode = "17046",
                deliveryRequest = "부재시 경비실에 맡겨주세요."
            )
        }
        println(response)
    }
}