package com.smilehunter.ablebody.presentation.payment.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentPassthroughData(
    val deliveryPrice: Int,
    val items: List<Item>
): Parcelable {
    val totalPrice = items.sumOf { it.price }

    @Parcelize
    data class Item(
        val itemID: Int,
        val brandName: String,
        val itemName: String,
        val price: Int,
        val salePrice: Int?,
        val salePercentage: Int?,
        val itemImageURL: String,
        val count: Int,
        val options: List<ItemOptions>
    ): Parcelable {
        val differencePrice = salePrice?.let { price - it } ?: 0
    }

    @Parcelize
    data class ItemOptions(
        val id: Long,
        val content: String,  // color: 블랙, 레드, 화이트 size: S, M, L
        val options: Option
    ): Parcelable {
        enum class Option {
            COLOR, SIZE
        }
    }
}