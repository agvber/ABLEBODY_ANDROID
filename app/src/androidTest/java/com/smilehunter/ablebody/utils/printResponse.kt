package com.smilehunter.ablebody.utils

import android.util.Log
import org.junit.Assert
import retrofit2.Response

internal fun<T> printResponse(response: Response<T>) {
    Log.d("request", response.raw().request.toString())
    Log.d("response", response.toString())
    Log.d("body", response.body().toString())
    Assert.assertEquals(response.code(), 200)
}