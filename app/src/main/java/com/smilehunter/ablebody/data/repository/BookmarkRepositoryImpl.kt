package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.AbleBodyResponse
import com.smilehunter.ablebody.data.dto.response.data.ReadBookmarkCodyData
import com.smilehunter.ablebody.data.dto.response.data.ReadBookmarkItemData
import com.smilehunter.ablebody.network.NetworkService
import retrofit2.Response
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
): BookmarkRepository {
    override suspend fun addBookmarkItem(itemID: Long): Response<AbleBodyResponse<String>> {
        return networkService.addBookmarkItem(itemID)
    }

    override suspend fun readBookmarkItem(
        page: Int,
        size: Int
    ): Response<AbleBodyResponse<ReadBookmarkItemData>> {
        return networkService.readBookmarkItem(page, size)
    }

    override suspend fun deleteBookmarkItem(itemID: Long): Response<AbleBodyResponse<String>> {
        return networkService.deleteBookmarkItem(itemID)
    }

    override suspend fun addBookmarkCody(itemID: Long): Response<AbleBodyResponse<String>> {
        return networkService.addBookmarkCody(itemID)
    }

    override suspend fun readBookmarkCody(
        page: Int,
        size: Int
    ): Response<AbleBodyResponse<ReadBookmarkCodyData>> {
        return networkService.readBookmarkCody(page, size)
    }

    override suspend fun deleteBookmarkCody(itemID: Long): Response<AbleBodyResponse<String>> {
        return networkService.deleteBookmarkCody(itemID)
    }
}