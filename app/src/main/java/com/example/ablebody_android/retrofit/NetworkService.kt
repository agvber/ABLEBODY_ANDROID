package com.example.ablebody_android.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
    private const val MAIN_SERVER_URL = "https://aws.ablebody.im:50913"
    private const val TEST_SERVER_URL = "https://aws.ablebody.im:40913"


    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(TEST_SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}