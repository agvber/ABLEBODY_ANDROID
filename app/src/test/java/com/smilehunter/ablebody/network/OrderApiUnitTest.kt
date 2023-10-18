package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.network.utils.TestRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Test

class OrderApiUnitTest {
    private val networkService: NetworkService = TestRetrofit.getInstance()

    @Test
    fun addOrderList() {
        val response = runBlocking {
            networkService.addOrderList(
                itemID = 52,
                addressID = 190,
                couponBagsID = 10,
                refundBankName = "KB국민은행",
                refundAccount = "72560100011434",
                refundAccountHolder = "이재휘",
                paymentMethod = "CASH",
                price = 35000,
                itemDiscount = 0,
                couponDiscount = 0,
                pointDiscount = 3500,
                deliveryPrice = 0,
                amountOfPayment = 3000,
                itemOptionIdList = listOf(1, 3)
            )
        }
        println(response)
    }

    @Test
    fun getOrderList() {
        val response = runBlocking {
            networkService.getOrderList()
        }
        println(response)
    }

    @Test
    fun cancelOrderList() {
        val response = runBlocking {
            networkService.cancelOrderList(id = "202310180002")
        }
        println(response)
    }

    @Test
    fun getDeliveryInfo() {
        val response = runBlocking {
            networkService.getDeliveryInfo(id = "202310180002")
        }
        println(response)
    }
}