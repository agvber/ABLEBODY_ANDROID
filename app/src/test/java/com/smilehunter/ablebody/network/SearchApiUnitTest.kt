package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.network.utils.TestRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SearchApiUnitTest {
    private val networkService: NetworkService = TestRetrofit.getInstance()

    @Test
    fun uniSearch() {
        val response = runBlocking { networkService.uniSearch("") }
        println(response.toString())
    }

    @Test
    fun searchItem() {
        val response = runBlocking {
            networkService.searchItem(
                sort = SortingMethod.POPULAR,
                keyword = "나이키",
                itemGender = ItemGender.UNISEX,
                parentCategory = ItemParentCategory.ALL
            )
        }
        println(response.toString())
    }

    @Test
    fun searchCody() {
        val response = runBlocking {
            networkService.searchCody(
                keyword = "나이키",
                category = listOf(),
                genders = listOf()
            )
        }
        println(response.toString())
    }
}