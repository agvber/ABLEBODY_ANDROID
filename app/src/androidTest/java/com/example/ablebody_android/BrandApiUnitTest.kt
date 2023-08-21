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

    private val tokenSharedPreferencesRepository = TokenSharedPreferencesRepository(applicationContext = appContext)
    private val networkRepository = NetworkRepository(tokenSharedPreferencesRepository)

    private val authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiODc1NTM4MCIsImV4cCI6MTY4OTE1MTM0N30.o96O1ijkZreWOMHllSHxcxR3xQ6yvlvA7j1XneXORoq_7aquOOwxMTa-ShFeOUdp0PaqiYm2ZIdMTSqXarqJjA"
    private val refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwidWlkIjoiODc1NTM4MCIsImV4cCI6MTY5MDQ0OTg2OH0.j9j-yOX5JNt4ENCA-6A4KuLpNoBK6CiBwLbJwW1nuPVZEUFJYp1DxVs03p3eUg89qG8CJNZ-BgieN4oaWG6MXA"

//    @Test
//    fun brandMain(){
//        val response = networkRepository.brandMain(
//            authToken = authToken,
//            sort = SortingMethod.POPULAR,
//
//        )
//
//        println("response: $response, body: ${response.body()}")
//        Assert.assertEquals(response.code(), 200)
//    }
//
//    @Test
//    fun brandDetaiItem(){
//        val response = networkRepository.brandDetaiItem(
//            authToken = authToken,
//            sort = SortingMethod.POPULAR,
//            brandId = 3,
//            itemGender = ItemGender.MALE,
//            parentCategory = ItemParentCategory.ALL,
//            childCategory = ItemChildCategory.SHORT_SLEEVE,
//            page = 0,
//            size = 20
//        )
//
//        println("response: $response, body: ${response.body()}")
//        Assert.assertEquals(response.code(), 200)
//    }
//
//    @Test
//    fun brandDetailCody(){
//        val response = networkRepository.brandDetailCody(
//            authToken = authToken,
//            brandId = 3,
//            gender = Gender.MALE,
//            category = HomeCategory.GYMWEAR,
//            height1 = null,
//            height2 = null,
//            page = 0,
//            size = 20
//        )
//
//        println("response: $response, body: ${response.body()}")
//        Assert.assertEquals(response.code(), 200)
//    }
}