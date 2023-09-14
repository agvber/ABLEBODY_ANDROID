package com.example.ablebody_android.data.repository

import com.example.ablebody_android.data.dto.response.AbleBodyResponse
import com.example.ablebody_android.data.dto.response.data.ReadBookmarkCodyData
import com.example.ablebody_android.data.dto.response.data.ReadBookmarkItemData
import retrofit2.Response

interface BookmarkRepository {
    suspend fun addBookmarkItem(itemID: Long): Response<AbleBodyResponse<String>>

    suspend fun readBookmarkItem(
        page: Int = 0,
        size: Int = 20
    ): Response<AbleBodyResponse<ReadBookmarkItemData>>

    suspend fun deleteBookmarkItem(itemID: Long): Response<AbleBodyResponse<String>>

    suspend fun addBookmarkCody(itemID: Long): Response<AbleBodyResponse<String>>

    suspend fun readBookmarkCody(
        page: Int = 0,
        size: Int = 20
    ): Response<AbleBodyResponse<ReadBookmarkCodyData>>

    suspend fun deleteBookmarkCody(itemID: Long): Response<AbleBodyResponse<String>>
}