package com.smilehunter.ablebody.model

data class PurchaseItemData (
    val brandName: String?,
    val itemName: String?,
    val itemColor: String?,
    val itemSize: String?,
    val price: Int?,
    val salePercent: Int?,
    val itemImage: String?,
    val deliveryFee: Long?
    // 브랜드 이름, 제품 이름, 제품 색상, 제품 사이즈, 가격, 세일 퍼센트, 제품 이미지 URL
)