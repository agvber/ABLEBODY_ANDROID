package com.example.ablebody_android.retrofit.dto.response.data

import com.example.ablebody_android.ItemGender

data class BrandMainResponseData(
    val name: String,
    val id: Int,
    val thumbnail: String,
    val subName: String,
    val brandGender: ItemGender,
    val maxDiscount: Int
)