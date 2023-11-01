package com.smilehunter.ablebody.presentation.delivery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.repository.UserRepository
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher.*
import com.smilehunter.ablebody.network.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
): ViewModel() {

    fun addAddress(
        attentionName: String,
        phoneNumber: String,
        roadAddress: String,
        roadDetailAddress: String,
        zipCode: String,
        deliveryRequestMessage: String
    ) {
        try {
            viewModelScope.launch(ioDispatcher) {
                if (roadAddress.isBlank()) {
                    userRepository.addMyAddress(
                        name = attentionName,
                        phoneNumber = phoneNumber,
                        roadAddress = roadAddress,
                        roadDetailAddress = roadDetailAddress,
                        zipCode = zipCode,
                        deliveryRequestMessage = deliveryRequestMessage
                    )
                } else {
                    userRepository.editMyAddress(
                        name = attentionName,
                        phoneNumber = phoneNumber,
                        roadAddress = roadAddress,
                        roadDetailAddress = roadDetailAddress,
                        zipCode = zipCode,
                        deliveryRequestMessage = deliveryRequestMessage
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

