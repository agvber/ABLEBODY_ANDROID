package com.example.ablebody_android

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BrandApiUnitTest {
    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private val tokenSharedPreferencesRepository =
        TokenSharedPreferencesRepository(applicationContext = appContext)
    private val networkRepository = NetworkRepository(tokenSharedPreferencesRepository)

    private val authToken =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiNjYzNDUxNCIsImV4cCI6MTY5Mjc5ODUxMH0.Fcp-eGchEUE2Hhjf9PxaXy705bgSCGLWDRypmeumavUu1ZcVOh0Jof5dN8ZFMzkMnfUtiHD_U5dAWZdU9YWrgg"
    private val refreshToken =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwidWlkIjoiNjYzNDUxNCIsImV4cCI6MTY5NDAwNjMxMH0.ULp-X1X8uBTXaQOrwofoz0T9BW-feLxJfyfTYGT2YIHt2l0fPl94zBl4YxZAHGEM_4Y0C6GQXp7nuWoCZ7gGAA"

    @Test
    fun brandMain() {
        val response = networkRepository.brandMain(
            sort = SortingMethod.POPULAR
        )
        println("response: $response, body: ${response.body()}")
        println("response: ${response.errorBody()}")
        Assert.assertEquals(response.code(), 200)
    }

    @Test
    fun brandDetaiItem() {
        val response = networkRepository.brandDetaiItem(
            authToken = authToken,
            sort = SortingMethod.POPULAR,
            brandId = 3,
            itemGender = ItemGender.MALE,
            parentCategory = ItemParentCategory.ALL,
            childCategory = ItemChildCategory.SHORT_SLEEVE,
            page = 0,
            size = 20
        )

        println("response: $response, body: ${response.body()}")
        Assert.assertEquals(response.code(), 200)
    }


    @Test
    fun brandDetailCody(){
        val genders: List<Gender> = listOf(Gender.MALE, Gender.FEMALE)
        val categories: List<HomeCategory> = listOf(HomeCategory.GYMWEAR, HomeCategory.PILATES, HomeCategory.RUNNING, HomeCategory.TENNIS)

//        val response = networkRepository.brandDetailCody(
//            authToken = authToken,
//            brandId = 3,
//            gender = listOf("MALE"),
//            category = categories,
//            height1 = null,
//            height2 = null,
//            page = 0,
//            size = 20
//        )
//
//        println("response: $response, body: ${response.body()}")
//        Assert.assertEquals(response.code(), 200)
    }
}