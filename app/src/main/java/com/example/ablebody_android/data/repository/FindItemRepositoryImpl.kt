package com.example.ablebody_android.data.repository

import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.data.dto.response.FindItemResponse
import com.example.ablebody_android.network.NetworkService
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