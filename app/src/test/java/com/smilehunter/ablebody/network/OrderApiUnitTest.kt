package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.data.dto.request.AddOrderListRequest
import com.smilehunter.ablebody.network.utils.TestRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class OrderApiUnitTest {
    private val networkService: NetworkService = TestRetrofit.getInstance()

    @Test
    fun addOrderList() {
        val response = runBlocking {
            networkService.addOrderList(
                addOrderListRequest = AddOrderListRequest(
                    addressId = 193,
                    orderName = "나이키 스포츠웨어 에센셜",
                    paymentType = "NORMAL",
                    paymentMethod = "CARD",
                    easyPayType = "토스페이",
                    pointDiscount = 1000,
                    deliveryPrice = 3000,
                    orderListItemReqDtoList = listOf(
                        AddOrderListRequest.OrderListItemReqDto(
                            itemId = 52,
                            couponBagsId = 132,
                            colorOption = "블랙",
                            sizeOption = "M",
                            itemPrice = 35000,
                            itemCount = 1,
                            itemDiscount = 6000,
                            couponDiscount = 3000,
                            amount = 26000
                        )
                    ),
                )
            )
        }
        println(response)
        Assert.assertTrue(response.success)
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

    @Test
    fun getOrderListDetail() {
        val response = runBlocking {
            networkService.getOrderListDetail(id = "202310270003")
        }
        println(response)
    }
}