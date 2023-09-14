package com.example.ablebody_android.network

import com.example.ablebody_android.network.utils.TestRetrofit
import com.example.ablebody_android.utils.printResponse
import kotlinx.coroutines.runBlocking
import org.junit.Test

class BookmarkApiUnitTest {
    private val networkService: NetworkService = TestRetrofit.getInstance()

    @Test
    fun addBookmarkItem() {
        val response = runBlocking { networkService.addBookmarkItem(0L) }
        printResponse(response)
    }

    @Test
    fun readBookmarkItem() {
        val response = runBlocking { networkService.readBookmarkItem() }
        printResponse(response)
    }

    @Test
    fun deleteBookmarkItem() {
        val response = runBlocking { networkService.deleteBookmarkItem(itemID = 0L) }
        printResponse(response)
    }

    @Test
    fun addBookmarkCody() {
        val response = runBlocking { networkService.addBookmarkCody(itemID = 0L) }
        printResponse(response)
    }

    @Test
    fun readBookmarkCody() {
        val response = runBlocking { networkService.readBookmarkCody() }
        printResponse(response)
    }

    @Test
    fun deleteBookmarkCody() {
        val response = runBlocking { networkService.deleteBookmarkCody(itemID = 0L) }
        printResponse(response)
    }

}