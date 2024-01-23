package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.HomeCategory
import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.data.dto.response.BrandDetailCodyResponse
import com.smilehunter.ablebody.data.dto.response.BrandDetailItemResponse
import com.smilehunter.ablebody.data.dto.response.BrandMainResponse
import com.smilehunter.ablebody.network.NetworkService
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
            sort = sort,
            brandId = brandId,
            itemGender = itemGender,
            parentCategory = childCategory?.parentCategory ?: parentCategory,
            childCategory = childCategory,
            page = page,
            size = size
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
            brandId = brandId,
            gender = gender,
            category = category,
            personHeightRangeStart = personHeightRangeStart,
            personHeightRangeEnd = personHeightRangeEnd,
            page = page,
            size = size
        )
}