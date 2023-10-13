package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.response.data.CreatorDetailResponseData
import com.smilehunter.ablebody.data.repository.CreatorDetailRepository
import com.smilehunter.ablebody.model.CreatorDetailData
import javax.inject.Inject

class GetCreatorDetailDataListUseCase @Inject constructor(
    private val creatorDetailRepository: CreatorDetailRepository
) {

    suspend operator fun invoke(id: Long, userID: String): CreatorDetailData {
        return creatorDetailRepository.getCreatorDetailData(id).toDomain(userID)
    }
}

private fun CreatorDetailResponseData.toDomain(uid: String) = CreatorDetailData(
    id = id,
    createDate = createDate,
    category = category,
    userInfo = CreatorDetailData.UserInfo(
        uid = creator.uid,
        nickname = creator.nickname,
        name = userInfo.name,
        height = userInfo.height,
        weight = userInfo.weight,
        gender = creator.gender,
        job = userInfo.job,
        profileImageURL = creator.profileUrl,
        instagramDeepLink = userInfo.instaDeepLink,
        instagramWebLink = userInfo.instaWebLink,
        youtubeDeepLink = userInfo.youtubeDeepLink,
        youtubeWebLink = userInfo.youtubeWebLink,
        favoriteExercise = userInfo.favoriteExercise,
        experienceExercise = userInfo.career
    ),
    imageURLList = images,
    postItems = postItems.map {
        CreatorDetailData.PositionItem(
            id = it.id,
            category = when (it.item.childCategory) {
                ItemChildCategory.SHORT_SLEEVE -> CreatorDetailData.PositionItem.Category.SHORT_SLEEVE
                ItemChildCategory.LONG_SLEEVE -> CreatorDetailData.PositionItem.Category.LONG_SLEEVE
                ItemChildCategory.SLEEVELESS -> CreatorDetailData.PositionItem.Category.SLEEVELESS
                ItemChildCategory.SWEAT_HOODIE -> CreatorDetailData.PositionItem.Category.SWEAT_HOODIE
                ItemChildCategory.PADTOP -> CreatorDetailData.PositionItem.Category.PADTOP
                ItemChildCategory.SHORTS -> CreatorDetailData.PositionItem.Category.SHORTS
                ItemChildCategory.PANTS -> CreatorDetailData.PositionItem.Category.PANTS
                ItemChildCategory.LEGGINGS -> CreatorDetailData.PositionItem.Category.LEGGINGS
                ItemChildCategory.ZIP_UP -> CreatorDetailData.PositionItem.Category.ZIP_UP
                ItemChildCategory.WINDBREAK -> CreatorDetailData.PositionItem.Category.WINDBREAK
                ItemChildCategory.CARDIGAN -> CreatorDetailData.PositionItem.Category.CARDIGAN
                ItemChildCategory.HEADWEAR -> CreatorDetailData.PositionItem.Category.HEADWEAR
                ItemChildCategory.GUARD -> CreatorDetailData.PositionItem.Category.GUARD
                ItemChildCategory.STRAP -> CreatorDetailData.PositionItem.Category.STRAP
                ItemChildCategory.BELT -> CreatorDetailData.PositionItem.Category.BELT
                ItemChildCategory.SHOES -> CreatorDetailData.PositionItem.Category.SHOES
                ItemChildCategory.ETC -> CreatorDetailData.PositionItem.Category.ETC
            },
            xPosition = it.xpos,
            yPosition = it.ypos,
            size = it.size,
            item = CreatorDetailData.PositionItem.Item(
                id = it.item.id,
                name = it.item.name,
                price = it.item.price,
                salePrice = it.item.salePrice,
                brand = CreatorDetailData.PositionItem.Item.Brand(
                    id = it.item.brand.id,
                    name = it.item.brand.name,
                    subName = it.item.brand.subName,
                    thumbnailURL = it.item.brand.thumbnail,
                    isLaunched = it.item.brand.isLaunched
                ),
                imageURLList = it.item.images,
                deleted = it.item.deleted

            ),
            hasReview = it.hasReview
        )
    },
    likes = likes,
    comments = comments,
    views = views,
    exerciseExperience = career,
    isLiked = likeUsers.find { it.uid == uid } != null,
    bookmarked = bookmarked
)