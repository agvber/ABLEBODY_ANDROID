package com.smilehunter.ablebody.presentation.item_detail.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.dto.response.ItemDetailResponse
import com.smilehunter.ablebody.data.repository.BookmarkRepository
import com.smilehunter.ablebody.data.repository.ItemRepository
import com.smilehunter.ablebody.data.repository.ItemRepositoryImpl
import com.smilehunter.ablebody.domain.GetItemOptionListUseCase
import com.smilehunter.ablebody.domain.ItemData
import com.smilehunter.ablebody.network.NetworkService
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val itemUseCase: GetItemOptionListUseCase,
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _bookMark = MutableStateFlow(false)
    val bookMark: StateFlow<Boolean> = _bookMark.asStateFlow()

    fun getData(id: Long){
        try {
            viewModelScope.launch {
                val colorList = itemUseCase.invoke(id).colorList
                Log.d("colorList", colorList.toString())
                _itemDetailLiveData.postValue(itemUseCase.invoke(id))
            }
        }catch (e: Exception){
           e.printStackTrace()
        }
    }

    private val _itemDetailLiveData = MutableLiveData<ItemData>()
    val itemDetailLiveData: LiveData<ItemData> = _itemDetailLiveData

    fun toggleBookMark(id: Long){

        viewModelScope.launch(ioDispatcher) {
            if (itemDetailLiveData.value?.bookmarked == true) {
                Log.d("bookMarkViewModel지워짐", bookMark.value.toString())
                bookmarkRepository.deleteBookmarkItem(id)
            } else {
                Log.d("bookMarkViewModel생김", bookMark.value.toString())
                bookmarkRepository.addBookmarkItem(id)
            }
        }
    }


}