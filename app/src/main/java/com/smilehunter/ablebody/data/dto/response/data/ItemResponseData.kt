package com.smilehunter.ablebody.data.dto.response.data

import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.NetworkAuthorityName

data class ItemResponseData(
    val createDate: String,
    val modifiedDate: String,
    val likes: Int,
    val comments: Int,
    val views: Int,
    val id: Int,
    val item: Item,
    val commentAndReplies: List<CommentAndReplies>,
    val likeUsers: List<User>,
    val itemReviews: List<ItemReview>,
    val homePosts: List<HomePost>,
    val itemOptionList: List<ItemOptionResponseDto>,
    val bookmarked: Boolean,
    val deleted: Boolean,
    val seller: Seller?,
    val itemDetailImages: List<ItemDetailImageResource>,
) {
    data class Item(
        val id: Long,
        val itemGender: ItemGender,
        val parentCategory: ItemParentCategory,
        val childCategory: ItemChildCategory,
        val name: String,
        val notionCode: String,
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
        data class Brand(
            val id: Long,
            val name: String,
            val subName: String?,
            val thumbnail: String,
            val brandGender: ItemGender?,
            val redirectUrl: String?,
            val isLaunched: Boolean
        )
    }
    data class CommentAndReplies(
        val type: CommentReplyType,
        val createDate: String,
        val modifiedDate: String,
        val id: Long,
        val writer: User,
        val contents: String,
        val likes: Int,
        val likeUsers: List<User>,
        val parentId: Long?
    ){
        data class CommentReplyType(
            val type: CommentReplyType,
            val createDate: String,
            val modifiedDate: String,
            val id: Long,
            val writer: User,
            val contents: String,
            val likes: Int,
            val likeUsers: List<User>,
            val parentId: Long?
        ){
            enum class CommentReplyType{
                COMMENT,
                REPLY
            }
        }
    }
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
        val creatorPoint: Int,
        val authorities: List<Authority>
    ){
        data class Authority(
            val authorityName: NetworkAuthorityName
        )
    }
    data class ItemReview(
        val createDate: String,
        val modifiedDate: String,
        val id: Long,
        val creator: User,
        val images: List<String>,
        val review: String,
        val size: String,
        val starRating: Float
    )
    data class HomePost(
        val id: Long,
        val imageURL: String,
        val createDate: String,
        val comments: Int,
        val likes: Int,
        val views: Int,
        val plural: Boolean
    )
    data class ItemOptionResponseDto(
        val id: Int,
        val content: String,
        val itemId: Int,
        val itemOptionDetailList: List<ItemOptionDetailResponseDto>
    ){
        data class ItemOptionDetailResponseDto(
            val id: Int,
            val content: String,
            val stock: Int,
            val price: Int
        )
    }

    data class Seller(
        val businessAndRepName: String?,
        val brand: String?,
        val businessNumber: String?,
        val mailOrderSalesReportNumber: String?,
        val contactNumber: String?,
        val emailAddress: String?,
        val address: String?
    )

    data class ItemDetailImageResource(
        val imageUrl: String,
        val imageOrder: Int
    )
}
