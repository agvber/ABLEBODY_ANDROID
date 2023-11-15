package com.smilehunter.ablebody.model

data class PurchaseItemData (
    val itemId: Long?,
    val brandName: String?,
    val itemName: String?,
    val itemColor: String?,
    val itemSize: String?,
    val price: Int?,
    val itemDiscount: Int?,
    val salePercent: Int?,
    val itemImage: String?,
    val deliveryPrice: Long?,
    val itemIdOptions: List<Long>?
)