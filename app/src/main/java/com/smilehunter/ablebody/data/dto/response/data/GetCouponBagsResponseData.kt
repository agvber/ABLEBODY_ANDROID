package com.smilehunter.ablebody.data.dto.response.data

data class GetCouponBagsResponseData(
    val id: Int,
    val brand: String?,
    val couponTitle: String,
    val hasUsed: CouponUsed,
    val couponType: CouponType,
    val discountType: DiscountType,
    val content: String,
    val expirationDateStr: Int,
    val couponCount: Int,
    val discountAmount: Int
) {
    enum class CouponUsed { USED, NOUSED }
    enum class CouponType { USER, BRAND }
    enum class DiscountType { PRICE, RATE }
}
