package com.example.ablebody_android.brand

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ablebody_android.ItemGender
import com.example.ablebody_android.NetworkRepository
import com.example.ablebody_android.SortingMethod
import com.example.ablebody_android.TokenSharedPreferencesRepository
import com.example.ablebody_android.brand.data.GenderFilterType
import com.example.ablebody_android.brand.data.OrderFilterType
import com.example.ablebody_android.retrofit.dto.response.data.BrandMainResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BrandViewModel(application: Application): AndroidViewModel(application) {

    private val tokenSharedPreferencesRepository = TokenSharedPreferencesRepository(application.applicationContext)
    private val networkRepository = NetworkRepository(tokenSharedPreferencesRepository)

    private val ioDispatcher = Dispatchers.IO

    init {
        tokenSharedPreferencesRepository.putAuthToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9" +
                ".eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiOTk5OTk5OSIsImV4cCI6MTc3OTkzNjE" +
                "0M30.Ewo_tMdZIksV-Y3F3jPNdeuA_4Z5N-yNTwZtF9qyIu6DC03Cga9bw6Zp7k1K2ESwmPHkxF7rWCisyp1LDYMONQ")
    }

    private val _orderFilterType = MutableStateFlow(OrderFilterType.Popularity)
    val orderFilterType = _orderFilterType.asStateFlow()

    fun updateOrderFilterType(orderFilterType: OrderFilterType) {
        viewModelScope.launch(ioDispatcher) {
            _orderFilterType.emit(orderFilterType)
        }
    }

    private val _genderFilterType = MutableStateFlow(GenderFilterType.ALL)
    val genderFilterType = _genderFilterType.asStateFlow()

    fun updateGenderFilterType(genderFilterType: GenderFilterType) {
        viewModelScope.launch(ioDispatcher) {
            _genderFilterType.emit(genderFilterType)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val brandItemList: StateFlow<List<BrandMainResponseData>?> =
        orderFilterType.flatMapLatest {
            when (it) {
                OrderFilterType.Popularity -> {
                    flowOf(networkRepository.brandMain(SortingMethod.POPULAR).body()?.data)
                }
                OrderFilterType.Name -> {
                    flowOf(networkRepository.brandMain(SortingMethod.ALPHABET).body()?.data)
                }
            }
        }
            .combine(genderFilterType) { data, gender ->
                data?.filter {
                    when (gender) {
                        GenderFilterType.ALL -> {
                            true
                        }
                        GenderFilterType.MALE -> {
                            it.brandGender == ItemGender.MALE
                        }
                        GenderFilterType.FEMALE -> {
                            it.brandGender == ItemGender.FEMALE
                        }
                    }
                }
            }
                .flowOn(ioDispatcher)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = null
                )
}