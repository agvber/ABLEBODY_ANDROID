package com.example.ablebody_android.domain

import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.data.dto.response.data.BrandDetailItemResponseData
import com.example.ablebody_android.data.repository.BrandRepository
import com.example.ablebody_android.model.ProductItemData
import javax.inject.Inject

class BrandProductItemUseCase @Inject constructor(
    private val brandRepository: BrandRepository
) {

    suspend operator fun invoke(
        sortingMethod: SortingMethod,
        brandID: Long,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory?,
        page: Int = 0,
        size: Int = 20
    ): ProductItemData =
        brandRepository.brandDetailItem(
            sortingMethod, brandID, itemGender, parentCategory, childCategory, page, size
        )
            .body()?.data?.toDomain()
            ?: ProductItemData(emptyList(), 0, true, 0, true)

}


private fun BrandDetailItemResponseData.toDomain() = ProductItemData(
    content = content.map { it.toDomain() },
    totalPages = totalPages,
    last = last,
    number = number,
    first = first
)

private fun BrandDetailItemResponseData.Item.toDomain() = ProductItemData.Item(
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