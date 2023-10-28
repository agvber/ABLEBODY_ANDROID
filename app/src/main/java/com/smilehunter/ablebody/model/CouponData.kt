package com.smilehunter.ablebody.model

data class CouponData(
    val id: Int,
    val brand: String?,
    val couponTitle: String,
    val content: String,
    val invalid: Boolean,
    val couponType: CouponType,
    val discountType: DiscountType,
    val expirationDate: Int,
    val couponCount: Int,
    val discountAmount: Int
) {
    enum class CouponType { USER, BRAND }
    enum class DiscountType { PRICE, RATE }
}