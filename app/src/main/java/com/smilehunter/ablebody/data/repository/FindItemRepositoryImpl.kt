package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.data.dto.response.FindItemResponse
import com.smilehunter.ablebody.network.NetworkService
import retrofit2.Response
import javax.inject.Inject

class FindItemRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
): FindItemRepository {
    override suspend fun findItem(
        sortingMethod: SortingMethod,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory?,
        page: Int,
        size: Int
    ): Response<FindItemResponse> {
        return networkService.findItem(sortingMethod, itemGender, parentCategory, childCategory, page, size)
    }

}