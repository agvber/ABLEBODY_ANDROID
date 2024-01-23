package com.smilehunter.ablebody.data.dto.response.data

import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.HomeCategory
import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.NetworkAuthorityName

data class CreatorDetailResponseData(
    val id: Long,
    val createDate: String,
    val modifiedDate: String,
    val category: HomeCategory,
    val userInfo: UserInfo,
    val images: List<String>,
    val postItems: List<PositionItem>,
    val likes: Int,
    val comments: Int,
    val views: Int,
    val likeUsers: List<User>,
    val career: Int,
    val creator: User,
    val commentAndReplies: List<CommentOrReply>,
    val bookmarked: Boolean
) {
    data class UserInfo(
        val name: String?,
        val height: Int?,
        val weight: Int?,
        val job: String?,
        val favoriteExercise: String?,
        val instaDeepLink: String?,
        val instaWebLink: String?,
        val youtubeDeepLink: String?,
        val youtubeWebLink: String?,
        val magazineId: String?,
        val interview: String?,
        val career: String?
    )

    data class PositionItem(
        val id: Long,
        val xpos: Double,
        val ypos: Double,
        val size: String,
        val item: Item,
        val hasReview: Boolean
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
            val deleted: Boolean
        ) {
            data class Brand(
                val id: Long,
                val name: String,
                val subName: String?,
                val thumbnail: String,
                val brandGender: ItemGender,
                val redirectUrl: String?,
                val isLaunched: Boolean
            )
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
        val profileUrl: String?,
        val introduction: String?,
        val creatorPoint: Int,
        val authorities: Set<Authority>
    )
    data class CommentOrReply(
        val type: CommentReplyType,
        val createDate: String,
        val modifiedDate: String,
        val id: Long,
        val writer: User,
        val contents: String,
        val likes: Int,
        val likeUsers: List<User>,
        val parentId: Long?
    ) {
        enum class CommentReplyType {
            COMMENT, REPLY
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
            val profileUrl: String?,
            val introduction: String?,
            val creatorPoint: Int,
            val authorities: Set<Authority>
        )
        enum class Gender { MALE, FEMALE }
        data class Authority(
            val authorityName: NetworkAuthorityName
        )
    }
    data class Authority(
        val authorityName: NetworkAuthorityName
    )
}