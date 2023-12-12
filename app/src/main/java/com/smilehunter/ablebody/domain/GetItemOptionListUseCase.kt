package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.dto.response.data.ItemResponseData
import com.smilehunter.ablebody.data.repository.ItemRepository
import com.smilehunter.ablebody.model.ItemDetailData
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher.IO
import com.smilehunter.ablebody.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetItemOptionListUseCase @Inject constructor(
    private val itemRepository: ItemRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
){
    suspend operator fun invoke(id: Long): ItemDetailData{

        return withContext(ioDispatcher) {
            itemRepository.itemDetail(id).data!!.toDomain()
        }
    }
}

fun ItemResponseData.toDomain() = ItemDetailData(
        id = this.id,
        item = ItemDetailData.Item(
            id = this.item.id,
            name = this.item.name,
            price = this.item.price,
            salePrice = this.item.salePrice,
            redirectUrl = this.item.redirectUrl,
            redirectText = this.item.redirectText,
            brand = ItemDetailData.Item.Brand(
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
             ItemDetailData.ItemReview(
                 createDate = it.createDate,
                 modifiedDate = it.modifiedDate,
                 id = it.id,
                 creator = ItemDetailData.ItemReview.User(
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
              ItemDetailData.HomePost(
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
             ItemDetailData.ItemOptionData(
                 id = itemOption.id,
                 content = itemOption.content,
                 itemId = itemOption.itemId,
                 itemOptionDetailList = itemOption.itemOptionDetailList.map { itemOptionDetail ->
                     ItemDetailData.ItemOptionData.ItemOptionDetailData(
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


