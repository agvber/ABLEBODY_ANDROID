package com.smilehunter.ablebody.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.smilehunter.ablebody.data.repository.NotificationRepository
import com.smilehunter.ablebody.domain.NotificationItemPagerUseCase
import com.smilehunter.ablebody.model.NotificationItemData
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val notificationRepository: NotificationRepository,
    notificationItemPagerUseCase: NotificationItemPagerUseCase
): ViewModel() {

    private val _networkRefreshFlow = MutableSharedFlow<Unit>()
    private val networkRefreshFlow = _networkRefreshFlow.asSharedFlow()

    fun refreshNetwork() {
        viewModelScope.launch { _networkRefreshFlow.emit(Unit) }
    }

    private val refreshItemIDList = MutableStateFlow(hashSetOf<Long>())

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingItemList: Flow<PagingData<NotificationItemData.Content>> =
        networkRefreshFlow.onSubscription { emit(Unit) }
            .flatMapLatest { notificationItemPagerUseCase() }
            .cachedIn(viewModelScope)
            .combine(refreshItemIDList) { pagingItemList, idList ->
                pagingItemList.map {
                    it.copy(checked = if (idList.contains(it.id) || idList.contains(-1)) true else it.checked)
                }
            }


    fun allCheck() {
        viewModelScope.launch(ioDispatcher) {
            if (!refreshItemIDList.value.contains(-1)) {
                notificationRepository.checkAllMyNoti()
                refreshItemIDList.emit(hashSetOf(-1))
            }
        }
    }

    fun itemCheck(id: Long) {
        viewModelScope.launch(ioDispatcher) {
            refreshItemIDList.emit(refreshItemIDList.value.toHashSet().apply { add(id) })
            notificationRepository.checkMyNoti(id)
        }
    }
}