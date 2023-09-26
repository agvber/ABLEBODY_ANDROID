package com.smilehunter.ablebody.network.utils

import com.smilehunter.ablebody.network.NetworkServiceImpl
import okhttp3.OkHttpClient

object TestRetrofit {

    private const val authToken =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiOTk5OTk5OSIsImV4cCI6MTc3OTkzNjE0M30.Ewo_tMdZIksV-Y3F3jPNdeuA_4Z5N-yNTwZtF9qyIu6DC03Cga9bw6Zp7k1K2ESwmPHkxF7rWCisyp1LDYMONQ"

    private val okHttpClient = OkHttpClient.Builder().run {
        addInterceptor {
            with(it) {
                val newRequest = request().newBuilder().run {
                    addHeader("Authorization", "Bearer $authToken")
                    build()
                }
                return@with proceed(newRequest)
            }
        }
        build()
    }

    fun getInstance() = NetworkServiceImpl(okHttpClient)
}