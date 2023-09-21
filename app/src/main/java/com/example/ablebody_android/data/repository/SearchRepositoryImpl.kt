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
import com.example.ablebody_android.database.dao.SearchHistoryDao
import com.example.ablebody_android.database.model.SearchHistoryEntity
import com.example.ablebody_android.network.NetworkService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val searchHistoryDao: SearchHistoryDao
): SearchRepository {
    override suspend fun uniSearch(keyword: String, page: Int, size: Int): UniSearchResponse {
        searchHistoryDao.insert(SearchHistoryEntity(keyword, System.currentTimeMillis()))
        return networkService.uniSearch(keyword, page, size)
    }

    override fun getSearchHistoryQueries(): Flow<List<SearchHistoryEntity>> =
        searchHistoryDao.getAll()

    override fun deleteAllSearchHistory() {
        searchHistoryDao.deleteAll()
    }

    override suspend fun searchItem(
        sort: SortingMethod,
        keyword: String,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory?,
        page: Int,
        size: Int
    ): SearchItemResponse {
        return networkService.searchItem(sort, keyword, itemGender, parentCategory, childCategory, page, size)
    }

    override suspend fun searchCody(
        keyword: String,
        genders: List<Gender>,
        category: List<HomeCategory>,
        personHeightRangeStart: Int?,
        personHeightRangeEnd: Int?,
        page: Int,
        size: Int
    ): SearchCodyResponse {
        return networkService.searchCody(keyword, genders, category, personHeightRangeStart, personHeightRangeEnd, page, size)
    }
}