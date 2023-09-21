package com.example.ablebody_android.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.data.dto.response.data.BrandDetailItemResponseData
import com.example.ablebody_android.data.dto.response.data.FindItemResponseData
import com.example.ablebody_android.data.repository.BrandRepository
import com.example.ablebody_android.data.repository.FindItemRepository
import com.example.ablebody_android.model.ProductItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductItemPagerUseCase @Inject constructor(
    private val brandRepository: BrandRepository,
    private val findItemRepository: FindItemRepository
) {

    operator fun invoke(
        productItemPagingSourceData: ProductItemPagingSourceData
    ): Flow<PagingData<ProductItemData.Item>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            initialKey = 0
        ) {
            ProductItemPagingSource(productItemPagingSourceData)
        }
            .flow


    private inner class ProductItemPagingSource(
        private val productItemPagingSourceData: ProductItemPagingSourceData
    ): PagingSource<Int, ProductItemData.Item>() {
        override fun getRefreshKey(state: PagingState<Int, ProductItemData.Item>): Int? =
            state.anchorPosition

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductItemData.Item> =
            try {
                val currentPageIndex = params.key ?: 0
                val productItemData: ProductItemData = withContext(Dispatchers.IO) {
                    when(productItemPagingSourceData) {
                        is ProductItemPagingSourceData.Brand -> {
                            brandRepository.brandDetailItem(
                                productItemPagingSourceData.sortingMethod,
                                productItemPagingSourceData.brandID,
                                productItemPagingSourceData.itemGender,
                                productItemPagingSourceData.itemParentCategory,
                                productItemPagingSourceData. itemChildCategory,
                                currentPageIndex
                            )
                                .body()?.data?.toDomain()
                        }
                        is ProductItemPagingSourceData.Item -> {
                            findItemRepository.findItem(
                                productItemPagingSourceData.sortingMethod,
                                productItemPagingSourceData.itemGender,
                                productItemPagingSourceData.itemParentCategory,
                                productItemPagingSourceData.itemChildCategory,
                                currentPageIndex
                            )
                                .body()?.data?.toDomain()
                        }
                    }
                }
                    ?: ProductItemData(emptyList(), 0, true, 0, true)

                LoadResult.Page(
                    data = productItemData.content,
                    prevKey = if (productItemData.first) null else productItemData.number - 1,
                    nextKey = if (productItemData.last) null else productItemData.number + 1
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
    }
}

sealed interface ProductItemPagingSourceData {
    data class Brand(
        val sortingMethod: SortingMethod,
        val brandID: Long,
        val itemGender: ItemGender,
        val itemParentCategory: ItemParentCategory,
        val itemChildCategory: ItemChildCategory?,
    ): ProductItemPagingSourceData

    data class Item(
        val sortingMethod: SortingMethod,
        val itemGender: ItemGender,
        val itemParentCategory: ItemParentCategory,
        val itemChildCategory: ItemChildCategory?,
    ): ProductItemPagingSourceData
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