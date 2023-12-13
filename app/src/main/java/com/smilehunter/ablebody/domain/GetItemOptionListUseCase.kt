package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.response.data.ItemResponseData
import com.smilehunter.ablebody.data.repository.ItemRepository
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetItemOptionListUseCase @Inject constructor(
    private val itemRepository: ItemRepository,
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
){
    suspend operator fun invoke(id: Long): ItemData{

        return withContext(ioDispatcher){
            itemRepository.itemDetail(id).data!!.toDomain()
        }
    }
}

fun ItemResponseData.toDomain() = ItemData(
        id = this.id,
        item = ItemData.Item(
            id = this.item.id,
            name = this.item.name,
            price = this.item.price,
            salePrice = this.item.salePrice,
            redirectUrl = this.item.redirectUrl,
            redirectText = this.item.redirectText,
            brand = ItemData.Item.Brand(
                id = this.item.brand.id,
                name = this.item.brand.name,
                subName = this.item.brand.subName,
                thumbnail = this.item.brand.thumbnail,
                isLaunched = this.item.brand.isLaunched
            ),
            images = this.item.images,
            avgStarRating = this.item.avgStarRating,
            deleted = this.item.deleted,
            deliveryFee = this.item.deliveryFee
        ),
         itemReviews = this.itemReviews.map {
             ItemData.ItemReview(
                 createDate = it.createDate,
                 modifiedDate = it.modifiedDate,
                 id = it.id,
                 creator = ItemData.User(
                     createDate = it.creator.createDate,
                     modifiedDate = it.creator.modifiedDate,
                     gender = it.creator.gender,
                     uid = it.creator.uid,
                     nickname = it.creator.nickname,
                     name = it.creator.name,
                     height = it.creator.height,
                     weight = it.creator.weight,
                     job = it.creator.job,
                     profileUrl = it.creator.profileUrl,
                     introduction = it.creator.introduction
                 ),
                 images = it.images,
                 review = it.review,
                 size = it.size,
                 starRating = it.starRating
             )
         },
         homePosts = this.homePosts.map {
              ItemData.HomePost(
                  id = it.id,
                  imageURL = it.imageURL,
                  createDate = it.createDate,
                  comments = it.likes,
                  likes = it.likes,
                  views = it.views,
                  plural = it.plural
              )
         },
         itemOptionList = this.itemOptionList.map{ itemOption ->
             ItemData.ItemOptionData(
                 id = itemOption.id,
                 content = itemOption.content,
                 itemId = itemOption.itemId,
                 itemOptionDetailList = itemOption.itemOptionDetailList.map { itemOptionDetail ->
                     ItemData.ItemOptionData.ItemOptionDetailData(
                         id = itemOptionDetail.id,
                         content = itemOptionDetail.content,
                         stock = itemOptionDetail.stock,
                         price = itemOptionDetail.price
                     )
                 }
             )
         },
         bookmarked = this.bookmarked,
         deleted = this.deleted

     )


data class ItemData(
    val id: Int,
    val item: Item,
    val itemReviews: List<ItemReview>,
    val homePosts: List<HomePost>,
    val itemOptionList: List<ItemOptionData>,
    val bookmarked: Boolean,
    val deleted: Boolean
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
        data class Brand(
            val id: Long,
            val name: String,
            val subName: String?,
            val thumbnail: String,
            val isLaunched: Boolean
        )
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
    )
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
}



