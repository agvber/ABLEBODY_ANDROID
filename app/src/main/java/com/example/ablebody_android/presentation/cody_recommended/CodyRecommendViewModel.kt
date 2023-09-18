package com.example.ablebody_android.presentation.cody_recommended

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ablebody_android.data.dto.Gender
import com.example.ablebody_android.data.dto.HomeCategory
import com.example.ablebody_android.data.dto.PersonHeightFilterType
import com.example.ablebody_android.domain.CodyItemPagerUseCase
import com.example.ablebody_android.model.CodyItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CodyRecommendViewModel @Inject constructor(
    codyItemPagerUseCase: CodyItemPagerUseCase
): ViewModel() {

    fun resetCodyItemFilter() {
        viewModelScope.launch {
            _codyItemListGenderFilter.emit(emptyList())
            _codyItemListSportFilter.emit(emptyList())
            _codyItemListPersonHeightFilter.emit(PersonHeightFilterType.ALL)
        }
    }

    private val _codyItemListGenderFilter = MutableStateFlow<List<Gender>>(listOf())
    val codyItemListGenderFilter = _codyItemListGenderFilter.asStateFlow()

    fun updateCodyItemListGendersFilter(genders: List<Gender>) {
        viewModelScope.launch { _codyItemListGenderFilter.emit(genders) }
    }

    private val _codyItemListSportFilter = MutableStateFlow<List<HomeCategory>>(listOf())
    val codyItemListSportFilter = _codyItemListSportFilter.asStateFlow()

    fun updateCodyItemListSportFilter(sports: List<HomeCategory>) {
        viewModelScope.launch { _codyItemListSportFilter.emit(sports) }
    }

    private val _codyItemListPersonHeightFilter = MutableStateFlow(PersonHeightFilterType.ALL)
    val codyItemListPersonHeightFilter = _codyItemListPersonHeightFilter.asStateFlow()

    fun updateCodyItemListPersonHeightFilter(sports: PersonHeightFilterType) {
        viewModelScope.launch { _codyItemListPersonHeightFilter.emit(sports) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val codyPagingItem: Flow<PagingData<CodyItemData.Item>> =
        combine(
            codyItemListGenderFilter,
            codyItemListSportFilter,
            codyItemListPersonHeightFilter
        ) { gender, sport, height ->
            codyItemPagerUseCase(gender, sport, height.rangeStart, height.rangeEnd)
        }
            .flatMapLatest {
                it.cachedIn(viewModelScope)
            }
}