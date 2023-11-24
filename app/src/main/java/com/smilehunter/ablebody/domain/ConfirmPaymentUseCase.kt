package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.repository.OrderManagementRepository
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher.IO
import com.smilehunter.ablebody.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConfirmPaymentUseCase @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val orderManagementRepository: OrderManagementRepository
) {

    suspend operator fun invoke(
        paymentKey: String,
        orderListId: String,
        amount: String
    ) {
        withContext(ioDispatcher) {
            orderManagementRepository.confirmPayment(
                paymentKey = paymentKey, orderListId = orderListId, amount = amount
            )
        }
    }

}