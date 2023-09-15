package com.example.ablebody_android.domain

import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.data.dto.response.data.FindItemResponseData
import com.example.ablebody_android.data.repository.ItemRepository
import com.example.ablebody_android.model.ProductItemData
import javax.inject.Inject

class ItemUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(
        sortingMethod: SortingMethod,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory? = null,
        page: Int = 0,
        size: Int = 20
    ): ProductItemData =
        itemRepository
            .findItem(sortingMethod,itemGender, parentCategory, childCategory, page, size)
            .body()
            ?.data
            ?.toDomain()
            ?: ProductItemData(emptyList(), 0, true, 0, true)

}

private fun FindItemResponseData.toDomain() = ProductItemData(
    content = content.map { it.toDomain() },
    totalPages = totalPages,
    last = last,
    number = number,
    first = first
)

private fun FindItemResponseData.Item.toDomain() = ProductItemData.Item(
    id = id,
    name = name,
    price = price,
    salePrice = salePrice,
    brandName = brandName,
    imageURL = image,
    isSingleImage = isPlural,
    url = url,
    avgStarRating = avgStarRating
)