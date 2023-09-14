package com.example.ablebody_android.data.repository

import com.example.ablebody_android.data.dto.Gender
import com.example.ablebody_android.data.dto.HomeCategory
import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.data.dto.response.BrandDetailCodyResponse
import com.example.ablebody_android.data.dto.response.BrandDetailItemResponse
import com.example.ablebody_android.data.dto.response.BrandMainResponse
import com.example.ablebody_android.network.NetworkService
import retrofit2.Response
import javax.inject.Inject

class BrandRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
): BrandRepository {
    override suspend fun brandMain(sort: SortingMethod): Response<BrandMainResponse> {
        return networkService.brandMain(sort)
    }

    override suspend fun brandDetailItem(
        sort: SortingMethod,
        brandId: Long,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory?,
        page: Int?,
        size: Int?
    ): Response<BrandDetailItemResponse> =
        networkService.brandDetailItem(
            sort,
            brandId,
            itemGender,
            parentCategory,
            childCategory,
            page,
            size
        )


    override suspend fun brandDetailCody(
        brandId: Long,
        gender: List<Gender>,
        category: List<HomeCategory>,
        personHeightRangeStart: Int?,
        personHeightRangeEnd: Int?,
        page: Int?,
        size: Int?
    ): Response<BrandDetailCodyResponse> =
        networkService.brandDetailCody(
            brandId,
            gender,
            category,
            personHeightRangeStart,
            personHeightRangeEnd,
            page,
            size
        )
}