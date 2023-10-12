package com.smilehunter.ablebody.model

import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.HomeCategory
import com.smilehunter.ablebody.utils.calculateElapsedTime
import kotlin.math.roundToInt

data class CreatorDetailData(
    val id: Long,
    val createDate: String,
    val category: HomeCategory,
    val userInfo: UserInfo,
    val imageURLList: List<String>,
    val postItems: List<PositionItem>,
    val likes: Int,
    val comments: Int,
    val views: Int,
    val exerciseExperience: Int,
    val commentAndReplies: List<CommentOrReply>,
    val isLiked: Boolean,
    val bookmarked: Boolean
) {
    val elapsedTime = calculateElapsedTime(createDate)
    data class UserInfo(
        val uid: String,
        val nickname: String,
        val name: String?,
        val height: Int?,
        val weight: Int?,
        val gender: Gender,
        val job: String?,
        val profileImageURL: String?,
        val instagramDeepLink: String?,
        val instagramWebLink: String?,
        val youtubeDeepLink: String?,
        val youtubeWebLink: String?,
        val favoriteExercise: String?,
        val experienceExercise: String?,
    ) {
        val experienceExerciseElapsedTime = experienceExercise?.let { calculateElapsedTime("${it}T00:00:00") }
    }
    data class PositionItem(
        val id: Long,
        val category: Category,
        val xPosition: Double,
        val yPosition: Double,
        val size: String,
        val item: Item,
        val hasReview: Boolean
    ) {
        enum class Category {
            SHORT_SLEEVE,
            LONG_SLEEVE,
            SLEEVELESS,
            SWEAT_HOODIE,
            PADTOP,
            SHORTS,
            PANTS,
            LEGGINGS,
            ZIP_UP,
            WINDBREAK,
            CARDIGAN,
            HEADWEAR,
            GUARD,
            STRAP,
            BELT,
            SHOES,
            ETC
        }
        data class Item(
            val id: Long,
            val name: String,
            val price: Int,
            val salePrice: Int?,
            val brand: Brand,
            val imageURLList: List<String>,
            val deleted: Boolean
        ) {
            val salePercentage = salePrice?.let { ((price - salePrice).toDouble() / price * 100).roundToInt() }
            data class Brand(
                val id: Long,
                val name: String,
                val subName: String?,
                val thumbnailURL: String,
                val isLaunched: Boolean
            )
        }
    }


    data class CommentOrReply(
        val type: CommentReplyType,
        val createDate: String,
        val modifiedDate: String,
        val id: Long,
        val writer: User,
        val contents: String,
        val likeCount: Int,
        val parentID: Long?,
        val isLiked: Boolean
    ) {
        val elapsedTime = calculateElapsedTime(createDate)
        enum class CommentReplyType { COMMENT, REPLY }

        data class User(
            val uid: String,
            val nickname: String,
            val name: String,
            val profileUrl: String?,
        )
    }
}