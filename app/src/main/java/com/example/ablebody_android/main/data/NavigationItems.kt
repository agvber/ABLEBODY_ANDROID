package com.example.ablebody_android.main.data

import com.example.ablebody_android.R

enum class NavigationItems(
    val labelText: String,
    val imageEnableResourceID: Int,
    val imageDisableResourceID: Int
) {
    Brand(labelText = "브랜드", imageEnableResourceID = R.drawable.ic_navigation_brand_enable, imageDisableResourceID = R.drawable.ic_navigation_brand_disable),
    Item(labelText ="아이템", imageEnableResourceID = R.drawable.ic_navigation_item_disable, imageDisableResourceID = R.drawable.ic_navigation_item_disable),
    CodyRecommendation(labelText ="코디 추천", imageEnableResourceID = R.drawable.ic_navigation_cody_recommend_disable, imageDisableResourceID = R.drawable.ic_navigation_cody_recommend_disable),
    Bookmark(labelText ="북마크", imageEnableResourceID = R.drawable.ic_bookmark_fill, imageDisableResourceID = R.drawable.ic_bookmark_empty),
    My(labelText ="내정보", imageEnableResourceID = R.drawable.ic_navigation_my_enable, imageDisableResourceID = R.drawable.ic_navigation_my_disable)
}