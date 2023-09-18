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
import com.example.ablebody_android.data.repository.ItemRepository
import com.example.ablebody_android.model.ProductItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductItemAutoPagerUseCase @Inject constructor(
    private val brandRepository: BrandRepository,
    private val itemRepository: ItemRepository
) {
    operator fun invoke(
        sortingMethod: SortingMethod,
        brandID: Long,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory?,
        page: Int = 0,
        size: Int = 20
    ): Flow<PagingData<ProductItemData.Item>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            initialKey = 0
        ) {
            BrandProductItemPagingSource(sortingMethod, brandID, parentCategory, childCategory, itemGender)
        }
            .flow

    operator fun invoke(
        sortingMethod: SortingMethod,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory? = null,
        page: Int = 0,
        size: Int = 20
    ): Flow<PagingData<ProductItemData.Item>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            initialKey = 0
        ) {
            ItemProductItemPagingSource(sortingMethod, parentCategory, childCategory, itemGender)
        }
            .flow

    inner class ItemProductItemPagingSource(
        private val sortingMethod: SortingMethod,
        private val itemParentCategory: ItemParentCategory,
        private val itemChildCategory: ItemChildCategory?,
        private val itemGender: ItemGender
    ): PagingSource<Int, ProductItemData.Item>() {
        override fun getRefreshKey(state: PagingState<Int, ProductItemData.Item>): Int? {
            return state.anchorPosition
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductItemData.Item> {
            return try {
                val currentPageIndex = params.key ?: 0
                val productItemData = withContext(Dispatchers.IO) {
                    itemRepository.findItem(sortingMethod, itemGender, itemParentCategory, itemChildCategory, currentPageIndex)
                }
                    .body()?.data?.toDomain()
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

    inner class BrandProductItemPagingSource(
        private val sortingMethod: SortingMethod,
        private val brandID: Long,
        private val itemParentCategory: ItemParentCategory,
        private val itemChildCategory: ItemChildCategory?,
        private val itemGender: ItemGender
    ): PagingSource<Int, ProductItemData.Item>() {
        override fun getRefreshKey(state: PagingState<Int, ProductItemData.Item>): Int? {
            return state.anchorPosition
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductItemData.Item> {
            return try {
                val currentPageIndex = params.key ?: 0
                val productItemData = withContext(Dispatchers.IO) {
                    brandRepository.brandDetailItem(sortingMethod, brandID, itemGender, itemParentCategory, itemChildCategory, currentPageIndex)
                }
                    .body()?.data?.toDomain()
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