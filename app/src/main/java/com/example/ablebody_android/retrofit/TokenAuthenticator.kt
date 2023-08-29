package com.example.ablebody_android.retrofit

import com.example.ablebody_android.NetworkRepository
import com.example.ablebody_android.TokenSharedPreferencesRepository
import com.example.ablebody_android.retrofit.dto.response.RefreshTokenResponse
import com.example.ablebody_android.retrofit.dto.response.data.RefreshTokenResponseData
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenSharedPreferencesRepository: TokenSharedPreferencesRepository,
    private val networkRepository: NetworkRepository
): Authenticator {
    private var retryCount = 0
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401 && retryCount < 5) {
            retryCount += 1
            val refreshTokenResponse = getRefreshTokenResponse()

            if (refreshTokenResponse?.isSuccessful != true) {
                networkRepository.invalidRefreshToken()
                return null
            }

            putToken(refreshTokenResponse.body()!!.data!!)

            return buildResponse(response, refreshTokenResponse.body()?.data!!.authToken)
        }
        return null
    }

    private fun getRefreshTokenResponse(): retrofit2.Response<RefreshTokenResponse>?  {
        val refreshToken = tokenSharedPreferencesRepository.getRefreshToken() ?: return null
        return networkRepository.getRefreshToken(refreshToken)
    }


    private fun putToken(data: RefreshTokenResponseData) {
        tokenSharedPreferencesRepository.putAuthToken(data.authToken)
        tokenSharedPreferencesRepository.putRefreshToken(data.refreshToken)
    }

    private fun buildResponse(response: Response, authToken: String) =
        response.request.newBuilder()
            .header("Authorization", "Bearer $authToken")
            .build()
}