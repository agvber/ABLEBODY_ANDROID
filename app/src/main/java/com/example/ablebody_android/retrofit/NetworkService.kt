package com.example.ablebody_android.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
    private const val MAIN_SERVER_URL = "https://aws.ablebody.im:50913"
    private const val TEST_SERVER_URL = "https://aws.ablebody.im:40913"


    private val client = OkHttpClient.Builder().build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(TEST_SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}