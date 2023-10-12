package com.smilehunter.ablebody.presentation.item_detail.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.dto.response.ItemDetailResponse
import com.smilehunter.ablebody.data.repository.ItemRepository
import com.smilehunter.ablebody.data.repository.ItemRepositoryImpl
import com.smilehunter.ablebody.network.NetworkService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    fun getData(id: Long){
        viewModelScope.launch {
            _itemDetailLiveData.postValue(itemRepository.itemDetail(id))
        }
    }

    private val _itemDetailLiveData = MutableLiveData<ItemDetailResponse>()
    val itemDetailLiveData: LiveData<ItemDetailResponse> = _itemDetailLiveData



}