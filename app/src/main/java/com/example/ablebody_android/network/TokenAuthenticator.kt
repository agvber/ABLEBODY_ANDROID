package com.example.ablebody_android.network

import com.example.ablebody_android.data.dto.response.RefreshTokenResponse
import com.example.ablebody_android.data.dto.response.data.RefreshTokenResponseData
import com.example.ablebody_android.sharedPreferences.TokenSharedPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Provider

class TokenAuthenticator constructor(
    private val tokenSharedPreferences: Provider<TokenSharedPreferences>,
    private val networkService: Provider<NetworkService>
) : Authenticator {
    private var retryCount = 0
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401 && retryCount < 5) {
            retryCount += 1
            val refreshTokenResponse = runBlocking { getRefreshTokenResponse() }

            if (refreshTokenResponse?.isSuccessful != true) {
                runBlocking { tokenSharedPreferences.get().clear() }
                return null
            }
            putToken(refreshTokenResponse.body()!!.data!!)
            return buildResponse(response, refreshTokenResponse.body()?.data!!.authToken)
        }
        return null
    }

    private suspend fun getRefreshTokenResponse(): retrofit2.Response<RefreshTokenResponse>? {
        val refreshToken = tokenSharedPreferences.get().getRefreshToken() ?: return null
        return networkService.get().getRefreshToken(refreshToken = refreshToken)
    }

    private fun putToken(data: RefreshTokenResponseData) {
        tokenSharedPreferences.get().putAuthToken(data.authToken)
        tokenSharedPreferences.get().putRefreshToken(data.refreshToken)
    }

    private fun buildResponse(response: Response, authToken: String) =
        response.request.newBuilder()
            .header("Authorization", "Bearer $authToken")
            .build()
}