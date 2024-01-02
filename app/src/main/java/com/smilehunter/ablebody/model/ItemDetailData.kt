package com.smilehunter.ablebody.model

import android.os.Parcelable
import com.smilehunter.ablebody.data.dto.Gender
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

data class ItemDetailData(
    val id: Int,
    val item: Item,
    val itemReviews: List<ItemReview>,
    val homePosts: List<HomePost>,
    val itemOptionList: List<ItemOptionData>,
    val bookmarked: Boolean,
    val deleted: Boolean,
    val seller: Seller,
    val detailImageUrls: List<String>
) {
    val colorList = itemOptionList.firstOrNull {
        it.content == "색상"
    }?.itemOptionDetailList?.map { it.content }

    val sizeList = itemOptionList.firstOrNull {
        it.content == "사이즈"
    }?.itemOptionDetailList?.map { it.content }

    data class Item(
        val id: Long,
        val name: String,
        val price: Int,
        val salePrice: Int?,
        val redirectUrl: String,
        val redirectText: String,
        val brand: Brand,
        val images: List<String>,
        val avgStarRating: String?,
        val deliveryFee: Long?,
        val deleted: Boolean
    ) {
        val salePercentage = salePrice?.let { ((price - salePrice).toDouble() / price * 100).roundToInt() }
        data class Brand(
            val id: Long,
            val name: String,
            val subName: String?,
            val thumbnail: String,
            val isLaunched: Boolean
        )
    }

    @Parcelize
    data class ItemReview(
        val createDate: String,
        val modifiedDate: String,
        val id: Long,
        val creator: User,
        val images: List<String>,
        val review: String,
        val size: String,
        val starRating: Float
    ): Parcelable {
        @Parcelize
        data class User(
            val createDate: String,
            val modifiedDate: String,
            val gender: Gender,
            val uid: String,
            val nickname: String,
            val name: String,
            val height: Int?,
            val weight: Int?,
            val job: String?,
            val profileUrl: String,
            val introduction: String?,
        ): Parcelable
    }

    data class HomePost(
        val id: Long,
        val imageURL: String,
        val createDate: String,
        val comments: Int,
        val likes: Int,
        val views: Int,
        val plural: Boolean
    )
    data class ItemOptionData(
        val id: Int,
        val content: String,
        val itemId: Int,
        val itemOptionDetailList: List<ItemOptionDetailData>
    ){
        data class ItemOptionDetailData(
            val id: Int,
            val content: String,
            val stock: Int,
            val price: Int
        )
    }

    data class Seller(
        val businessName: String?,
        val brand: String?,
        val businessNumber: String?,
        val reportNumber: String?,
        val contactNumber: String?,
        val emailAddress: String?,
        val roadAddress: String?
    )
}
