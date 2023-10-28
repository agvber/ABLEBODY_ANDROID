package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.dto.response.data.GetDeliveryInfoResponseData
import com.smilehunter.ablebody.data.repository.OrderManagementRepository
import com.smilehunter.ablebody.model.DeliveryTrackingData
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher.IO
import com.smilehunter.ablebody.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetDeliveryTrackingNumberUseCase @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val orderManagementRepository: OrderManagementRepository
) {

    suspend operator fun invoke(id: String): DeliveryTrackingData = withContext(ioDispatcher) {
        orderManagementRepository.getDeliveryTrackingNumber(id).data!!.toDomain()
    }
}

private fun GetDeliveryInfoResponseData.toDomain() =
    DeliveryTrackingData(
        id = id,
        deliveryCompanyName = deliveryCompanyName,
        trackingNumber = trackingNumber
    )