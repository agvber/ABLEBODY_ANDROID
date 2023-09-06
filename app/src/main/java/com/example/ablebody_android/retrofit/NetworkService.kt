package com.example.ablebody_android.retrofit

import com.example.ablebody_android.NetworkRepository
import com.example.ablebody_android.TokenSharedPreferencesRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
    private const val MAIN_SERVER_URL = "https://aws.ablebody.im:50913"
    private const val TEST_SERVER_URL = "https://aws.ablebody.im:40913"

    private var client: OkHttpClient? = null
    private var retrofit: Retrofit? = null

    fun getInstance(
        tokenSharedPreferencesRepository: TokenSharedPreferencesRepository,
        networkRepository: NetworkRepository
    ): Retrofit {

        if (client == null) {
            client = buildOkHttpClient(tokenSharedPreferencesRepository, networkRepository)
        }

        if (retrofit == null) {
            retrofit = buildRetrofit(client!!)
        }

        return retrofit!!
    }


    private fun buildOkHttpClient(
        tokenSharedPreferencesRepository: TokenSharedPreferencesRepository,
        networkRepository: NetworkRepository
    ) = OkHttpClient
        .Builder()
        .addInterceptor {
            with(it) {
                val newRequest = request().newBuilder()
                    .addHeader("Authorization", "Bearer ${tokenSharedPreferencesRepository.getAuthToken()}")
                    .build()
                return@with proceed(newRequest)
            }
        }
        .authenticator(TokenAuthenticator(tokenSharedPreferencesRepository, networkRepository))
        .build()


    private fun buildRetrofit(client: OkHttpClient) = Retrofit.Builder()
        .baseUrl(TEST_SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

}