package com.example.ablebody_android.network

import com.example.ablebody_android.data.dto.Gender
import com.example.ablebody_android.data.dto.HomeCategory
import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.network.utils.TestRetrofit
import com.example.ablebody_android.utils.printResponse
import kotlinx.coroutines.runBlocking
import org.junit.Test

class BrandApiUnitTest {
    private val networkService: NetworkService = TestRetrofit.getInstance()
    @Test
    fun brandMain() {
        val response = runBlocking {
            networkService.brandMain(
                sort = SortingMethod.POPULAR
            )
        }
        printResponse(response)
    }

    @Test
    fun brandDetailItem() {
        val response = runBlocking {
            networkService.brandDetailItem(
                sort = SortingMethod.POPULAR,
                brandId = 3,
                itemGender = ItemGender.MALE,
                parentCategory = ItemParentCategory.ALL,
                childCategory = ItemChildCategory.SHORT_SLEEVE,
                page = 0,
                size = 20
            )
        }
        printResponse(response)
    }


    @Test
    fun brandDetailCody() {
        val genders: List<Gender> = listOf(Gender.MALE, Gender.FEMALE)
        val categories: List<HomeCategory> = listOf(HomeCategory.GYMWEAR, HomeCategory.PILATES, HomeCategory.RUNNING, HomeCategory.TENNIS)

        val response = runBlocking {
            networkService.brandDetailCody(
                brandId = 3,
                gender = genders,
                category = categories,
                personHeightRangeStart = null,
                personHeightRangeEnd = null,
                page = 0,
                size = 20
            )
        }
        printResponse(response)
    }
}