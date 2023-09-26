package com.smilehunter.ablebody.utils

import org.junit.Assert
import retrofit2.Response

internal fun<T> printResponse(response: Response<T>) {
    println("Request: ${response.raw().request}")
    println("Response: $response")
    println("Body: ${response.body()}")
    Assert.assertEquals(response.code(), 200)
}