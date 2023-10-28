package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.dto.response.data.GetAddressResponseData
import com.smilehunter.ablebody.data.repository.UserRepository
import com.smilehunter.ablebody.model.DeliveryAddressData
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher.IO
import com.smilehunter.ablebody.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMyDeliveryAddressUseCase @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): DeliveryAddressData = withContext(ioDispatcher) {
        userRepository.getMyAddress().data!!.toDomain()
    }
}

private fun GetAddressResponseData.toDomain() =
    DeliveryAddressData(
        id = id,
        userName = receiverName,
        roadAddress = addressInfo,
        roadDetailAddress = detailAddress,
        zipCode = zipCode,
        phoneNumber = phoneNum,
        deliveryRequestMessage = deliveryRequest
    )