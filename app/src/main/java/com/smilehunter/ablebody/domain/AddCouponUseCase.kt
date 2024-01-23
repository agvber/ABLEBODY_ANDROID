package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.dto.response.AbleBodyResponse
import com.smilehunter.ablebody.data.repository.UserRepository
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddCouponUseCase @Inject constructor(
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(couponCode: String): CouponStatus = withContext(ioDispatcher) {
        val response = userRepository.addCouponByCouponCode(couponCode)
        if (response.success) {
            return@withContext CouponStatus.SUCCESS
        }

        when(response.errorCode) {
            AbleBodyResponse.ErrorCode.UNABLE_COUPON -> CouponStatus.UNABLE_COUPON
            AbleBodyResponse.ErrorCode.INVALID_COUPON_CODE -> CouponStatus.INVALID_COUPON_CODE
            AbleBodyResponse.ErrorCode.ALREADY_EXIST_COUPON -> CouponStatus.ALREADY_EXIST_COUPON
            else -> CouponStatus.UNKNOWN_ERROR
        }
    }
}

enum class CouponStatus {
    SUCCESS,
    UNKNOWN_ERROR,
    INVALID_COUPON_CODE,
    ALREADY_EXIST_COUPON,
    UNABLE_COUPON,
}