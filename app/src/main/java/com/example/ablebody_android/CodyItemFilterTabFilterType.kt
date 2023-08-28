package com.example.ablebody_android

enum class CodyItemFilterTabFilterType(
    val stringResourceID: Int,
    val isPopup: Boolean
    ) {
    MALE(stringResourceID = R.string.male, isPopup = false),
    FEMALE(stringResourceID = R.string.female, isPopup = false),
    SPORT(stringResourceID = R.string.cody_filter_type_sport, isPopup = true),
    HEIGHT(stringResourceID = R.string.cody_filter_type_height, isPopup = true)
}