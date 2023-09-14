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
import retrofit2.Response

interface BrandRepository {
    suspend fun brandMain(sort: SortingMethod): Response<BrandMainResponse>

    suspend fun brandDetailItem(
        sort: SortingMethod,
        brandId: Long,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory? = null,
        page: Int? = 0,
        size: Int? = 20
    ): Response<BrandDetailItemResponse>

    suspend fun brandDetailCody(
        brandId: Long,
        gender: List<Gender>,
        category: List<HomeCategory>,
        personHeightRangeStart: Int? = null,
        personHeightRangeEnd: Int? = null,
        page: Int? = 0,
        size: Int? = 20
    ): Response<BrandDetailCodyResponse>
}