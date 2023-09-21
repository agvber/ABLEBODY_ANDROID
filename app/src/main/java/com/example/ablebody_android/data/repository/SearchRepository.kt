package com.example.ablebody_android.data.repository

import com.example.ablebody_android.data.dto.Gender
import com.example.ablebody_android.data.dto.HomeCategory
import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.data.dto.response.SearchCodyResponse
import com.example.ablebody_android.data.dto.response.SearchItemResponse
import com.example.ablebody_android.data.dto.response.UniSearchResponse
import com.example.ablebody_android.database.model.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun uniSearch(
        keyword: String,
        page: Int = 0,
        size: Int = 10
    ): UniSearchResponse

    fun getSearchHistoryQueries(): Flow<List<SearchHistoryEntity>>

    fun deleteAllSearchHistory()
    suspend fun searchItem(
        sort: SortingMethod,
        keyword: String,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory? = null,
        page: Int = 0,
        size: Int = 20
    ): SearchItemResponse

    suspend fun searchCody(
        keyword: String,
        genders: List<Gender>,
        category: List<HomeCategory>,
        personHeightRangeStart: Int? = null,
        personHeightRangeEnd: Int? = null,
        page: Int = 0,
        size: Int = 20
    ): SearchCodyResponse
}