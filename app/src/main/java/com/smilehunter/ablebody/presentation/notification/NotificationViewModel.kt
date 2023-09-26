package com.smilehunter.ablebody.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.smilehunter.ablebody.data.repository.NotificationRepository
import com.smilehunter.ablebody.domain.NotificationItemPagerUseCase
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val notificationRepository: NotificationRepository,
    notificationItemPagerUseCase: NotificationItemPagerUseCase
): ViewModel() {

    val pagingItemList = notificationItemPagerUseCase().cachedIn(viewModelScope)

    fun allCheck() {
        viewModelScope.launch(ioDispatcher) {
            notificationRepository.checkAllMyNoti()
        }
    }

    fun itemCheck(id: Long) {
        viewModelScope.launch(ioDispatcher) {
            notificationRepository.checkMyNoti(id)
        }
    }
}