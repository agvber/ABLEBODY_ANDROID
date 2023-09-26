package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.HomeCategory
import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.network.utils.TestRetrofit
import com.smilehunter.ablebody.utils.printResponse
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