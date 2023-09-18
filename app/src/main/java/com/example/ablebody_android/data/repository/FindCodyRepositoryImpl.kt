package com.example.ablebody_android.data.repository

import com.example.ablebody_android.data.dto.Gender
import com.example.ablebody_android.data.dto.HomeCategory
import com.example.ablebody_android.data.dto.response.FindCodyResponse
import com.example.ablebody_android.network.NetworkService
import retrofit2.Response
import javax.inject.Inject

class FindCodyRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
): FindCodyRepository {
    override suspend fun findCody(
        genders: List<Gender>,
        category: List<HomeCategory>,
        personHeightRangeStart: Int?,
        personHeightRangeEnd: Int?,
        page: Int,
        size: Int
    ): Response<FindCodyResponse> {
        return networkService.findCody(genders, category, personHeightRangeStart, personHeightRangeEnd, page, size)
    }
}