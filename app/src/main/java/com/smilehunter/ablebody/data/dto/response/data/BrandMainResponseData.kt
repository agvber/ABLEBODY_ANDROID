package com.smilehunter.ablebody.data.dto.response.data

import com.google.gson.annotations.SerializedName

data class BrandMainResponseData(
    val name: String,
    val id: Long,
    val thumbnail: String,
    @SerializedName("sub_name") val subName: String,
    @SerializedName("brand_gender") val brandGender: com.smilehunter.ablebody.data.dto.ItemGender,
    @SerializedName("max_discount") val maxDiscount: Int
)