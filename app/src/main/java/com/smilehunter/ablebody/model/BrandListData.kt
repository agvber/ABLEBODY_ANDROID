package com.smilehunter.ablebody.model

import com.smilehunter.ablebody.data.dto.ItemGender

data class BrandListData(
    val name: String,
    val id: Long,
    val thumbnail: String,
    val subName: String,
    val brandGender: ItemGender,
    val maxDiscount: Int
)
