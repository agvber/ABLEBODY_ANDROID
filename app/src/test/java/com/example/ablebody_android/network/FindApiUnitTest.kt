package com.example.ablebody_android.network

import com.example.ablebody_android.data.dto.Gender
import com.example.ablebody_android.data.dto.HomeCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.network.utils.TestRetrofit
import com.example.ablebody_android.utils.printResponse
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FindApiUnitTest {
    private val networkAPI: NetworkService = TestRetrofit.getInstance()
    @Test
    fun findItem() {
        val response = runBlocking {
            networkAPI.findItem(
                itemGender = ItemGender.UNISEX,
                parentCategory = ItemParentCategory.ALL,
                childCategory = null
            )
        }
        printResponse(response)
    }

    @Test
    fun findCody() {
        val response = runBlocking {
            networkAPI.findCody(
                genders = listOf(Gender.MALE, Gender.FEMALE),
                category = listOf(HomeCategory.GYMWEAR)
            )
        }
        printResponse(response)
    }
}