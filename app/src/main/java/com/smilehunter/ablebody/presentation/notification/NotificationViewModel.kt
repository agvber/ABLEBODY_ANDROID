package com.smilehunter.ablebody.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.smilehunter.ablebody.data.repository.NotificationRepository
import com.smilehunter.ablebody.domain.NotificationItemPagerUseCase
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val notificationRepository: NotificationRepository,
    notificationItemPagerUseCase: NotificationItemPagerUseCase
): ViewModel() {

    private val refreshItemIDList = MutableStateFlow(hashSetOf<Long>())

    val pagingItemList = notificationItemPagerUseCase()
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