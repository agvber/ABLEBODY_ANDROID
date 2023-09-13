package com.example.ablebody_android

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ablebody_android.utils.printResponse
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FindApiUnitTest {
    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val tokenSharedPreferencesRepository =
        TokenSharedPreferencesRepository(applicationContext = appContext)
    private val networkRepository = NetworkRepository(tokenSharedPreferencesRepository)

    private val authToken =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiOTk5OTk5OSIsImV4cCI6MTc3OTkzNjE0M30.Ewo_tMdZIksV-Y3F3jPNdeuA_4Z5N-yNTwZtF9qyIu6DC03Cga9bw6Zp7k1K2ESwmPHkxF7rWCisyp1LDYMONQ"
    private val refreshToken =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwidWlkIjoiNjYzNDUxNCIsImV4cCI6MTY5NDAwNjMxMH0.ULp-X1X8uBTXaQOrwofoz0T9BW-feLxJfyfTYGT2YIHt2l0fPl94zBl4YxZAHGEM_4Y0C6GQXp7nuWoCZ7gGAA"

    @Before
    fun putToken() {
        tokenSharedPreferencesRepository.putAuthToken(authToken)
        tokenSharedPreferencesRepository.putRefreshToken(refreshToken)
    }

    @After
    fun checkToken() {
        val authToken = tokenSharedPreferencesRepository.getAuthToken()
        val refreshToken = tokenSharedPreferencesRepository.getRefreshToken()

        if (this.authToken != authToken) {
            Log.w("OnboardingUnitTest", "토큰이 변경되었습니다")
            Log.w("authToken", authToken.toString())
            Log.w("refreshToken", refreshToken.toString())
        }
    }

    @Test
    fun findItem() {
        val response = networkRepository.findItem(
            itemGender = ItemGender.UNISEX,
            parentCategory = ItemParentCategory.ALL,
            childCategory = null
        )
        printResponse(response)
    }

    @Test
    fun findCody() {
        val response = networkRepository.findCody(
            genders = listOf(Gender.MALE, Gender.FEMALE),
            category = listOf(HomeCategory.GYMWEAR)
        )
        printResponse(response)
    }
}