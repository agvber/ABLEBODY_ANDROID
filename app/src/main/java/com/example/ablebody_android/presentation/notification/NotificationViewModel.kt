package com.example.ablebody_android.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.ablebody_android.data.repository.NotificationRepository
import com.example.ablebody_android.domain.NotificationItemPagerUseCase
import com.example.ablebody_android.network.di.AbleBodyDispatcher
import com.example.ablebody_android.network.di.Dispatcher
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