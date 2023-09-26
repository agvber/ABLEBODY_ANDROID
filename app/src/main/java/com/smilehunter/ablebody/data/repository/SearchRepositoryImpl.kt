package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.HomeCategory
import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.data.dto.response.SearchCodyResponse
import com.smilehunter.ablebody.data.dto.response.SearchItemResponse
import com.smilehunter.ablebody.data.dto.response.UniSearchResponse
import com.smilehunter.ablebody.database.dao.SearchHistoryDao
import com.smilehunter.ablebody.database.model.SearchHistoryEntity
import com.smilehunter.ablebody.network.NetworkService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val searchHistoryDao: SearchHistoryDao
): SearchRepository {
    override suspend fun uniSearch(keyword: String, page: Int, size: Int): UniSearchResponse {
        return networkService.uniSearch(keyword, page, size)
    }

    override fun getSearchHistoryQueries(): Flow<List<SearchHistoryEntity>> =
        searchHistoryDao.getAll()

    override suspend fun deleteAllSearchHistory() {
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
        searchHistoryDao.insert(SearchHistoryEntity(keyword, System.currentTimeMillis()))
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
        searchHistoryDao.insert(SearchHistoryEntity(keyword, System.currentTimeMillis()))
        return networkService.searchCody(keyword, genders, category, personHeightRangeStart, personHeightRangeEnd, page, size)
    }
}