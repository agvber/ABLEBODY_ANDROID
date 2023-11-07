package com.smilehunter.ablebody.presentation.home.brand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.data.result.Result
import com.smilehunter.ablebody.data.result.asResult
import com.smilehunter.ablebody.domain.GetBrandListUseCase
import com.smilehunter.ablebody.presentation.home.brand.data.BrandListResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrandViewModel @Inject constructor(
    getBrandListUseCase: GetBrandListUseCase
): ViewModel() {

    private val _networkRefreshFlow = MutableSharedFlow<Unit>()
    private val networkRefreshFlow = _networkRefreshFlow.asSharedFlow()

    fun refreshNetwork() {
        viewModelScope.launch { _networkRefreshFlow.emit(Unit) }
    }

    private val _brandListSortingMethod = MutableStateFlow(SortingMethod.POPULAR)
    val brandListSortingMethod = _brandListSortingMethod.asStateFlow()

    fun updateBrandListOrderFilterType(sortingMethod: SortingMethod) {
        viewModelScope.launch {
            _brandListSortingMethod.emit(sortingMethod)
        }
    }

    private val _brandListGenderFilterType = MutableStateFlow(ItemGender.UNISEX)
    val brandListGenderFilterType = _brandListGenderFilterType.asStateFlow()

    fun updateBrandListGenderFilterType(gender: ItemGender) {
        viewModelScope.launch {
            _brandListGenderFilterType.emit(gender)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val brandItemList: StateFlow<BrandListResultUiState> =
        networkRefreshFlow.onSubscription { emit(Unit) }.flatMapLatest {
            brandListSortingMethod.flatMapLatest { sortingMethod ->
                flowOf(getBrandListUseCase(sortingMethod))
            }
                .combine(brandListGenderFilterType) { data, gender ->
                    data.filter {
                        when (gender) {
                            ItemGender.UNISEX -> true
                            else -> it.brandGender == ItemGender.UNISEX || it.brandGender == gender
                        }
                    }
                }
                .asResult()
                .map {
                    when (it) {
                        is Result.Success -> BrandListResultUiState.Success(it.data)
                        is Result.Loading -> BrandListResultUiState.Loading
                        is Result.Error -> BrandListResultUiState.Error
                    }
                }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = BrandListResultUiState.Loading
            )
}