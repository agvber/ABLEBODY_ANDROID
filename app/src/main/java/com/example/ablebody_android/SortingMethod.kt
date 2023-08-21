package com.example.ablebody_android

import com.google.gson.annotations.SerializedName

enum class SortingMethod {
    POPULAR,
    RECENT,
    @SerializedName("ALPHABET") NAME
}