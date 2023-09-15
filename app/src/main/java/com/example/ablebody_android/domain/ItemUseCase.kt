package com.example.ablebody_android.domain

import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.SortingMethod
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
    ): ProductItemData {
        val result = itemRepository.findItem(sortingMethod,itemGender, parentCategory, childCategory, page, size)

        return result.body()?.data?.let { data ->
            ProductItemData(
                content = data.content.map {
                    ProductItemData.Item(
                        id = it.id,
                        name = it.name,
                        price = it.price,
                        salePrice = it.salePrice,
                        brandName = it.brandName,
                        imageURL = it.image,
                        isSingleImage = it.isPlural,
                        url = it.url,
                        avgStarRating = it.avgStarRating
                    )
                },
                totalPages = data.totalPages,
                totalElements = data.totalElements,
                last = data.last,
                number = data.number,
                size = data.size,
                empty = data.empty,
                first = data.first,
                numberOfElements = data.numberOfElements
            )
        } ?: ProductItemData(emptyList(), 0, 0, true, 0, 0, 0, true, true)
    }
}