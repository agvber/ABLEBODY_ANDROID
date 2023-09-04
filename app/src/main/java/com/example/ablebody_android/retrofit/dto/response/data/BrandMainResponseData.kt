package com.example.ablebody_android.retrofit.dto.response.data

import com.example.ablebody_android.ItemGender
import com.google.gson.annotations.SerializedName

data class BrandMainResponseData(
    val name: String,
    val id: Long,
    val thumbnail: String,
    @SerializedName("sub_name") val subName: String,
    @SerializedName("brand_gender") val brandGender: ItemGender,
    @SerializedName("max_discount") val maxDiscount: Int
)