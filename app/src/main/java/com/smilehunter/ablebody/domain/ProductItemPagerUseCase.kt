package com.smilehunter.ablebody.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.data.dto.response.data.BrandDetailItemResponseData
import com.smilehunter.ablebody.data.dto.response.data.FindItemResponseData
import com.smilehunter.ablebody.data.dto.response.data.ReadBookmarkItemData
import com.smilehunter.ablebody.data.dto.response.data.SearchItemResponseData
import com.smilehunter.ablebody.data.repository.BookmarkRepository
import com.smilehunter.ablebody.data.repository.BrandRepository
import com.smilehunter.ablebody.data.repository.FindItemRepository
import com.smilehunter.ablebody.data.repository.SearchRepository
import com.smilehunter.ablebody.model.ProductItemData
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductItemPagerUseCase @Inject constructor(
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val brandRepository: BrandRepository,
    private val findItemRepository: FindItemRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val searchRepository: SearchRepository
) {

    operator fun invoke(
        productItemPagingSourceData: ProductItemPagingSourceData
    ): Flow<PagingData<ProductItemData.Item>> =
        Pager(
            config = PagingConfig(pageSize = 10),
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
                val productItemData: ProductItemData = withContext(ioDispatcher) {
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
                                .body()!!.data!!.toDomain()
                        }
                        is ProductItemPagingSourceData.Item -> {
                            findItemRepository.findItem(
                                productItemPagingSourceData.sortingMethod,
                                productItemPagingSourceData.itemGender,
                                productItemPagingSourceData.itemParentCategory,
                                productItemPagingSourceData.itemChildCategory,
                                currentPageIndex
                            )
                                .body()!!.data!!.toDomain()
                        }
                        is ProductItemPagingSourceData.Search -> {
                            searchRepository.searchItem(
                                productItemPagingSourceData.sortingMethod,
                                productItemPagingSourceData.keyword,
                                productItemPagingSourceData.itemGender,
                                productItemPagingSourceData.itemParentCategory,
                                productItemPagingSourceData.itemChildCategory,
                                currentPageIndex
                            )
                                .data!!.toDomain()
                        }

                        ProductItemPagingSourceData.Bookmark -> {
                            bookmarkRepository.readBookmarkItem(currentPageIndex)
                                .body()!!.data!!.toDomain()
                        }
                    }
                }

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

    data class Search(
        val sortingMethod: SortingMethod,
        val keyword: String,
        val itemGender: ItemGender,
        val itemParentCategory: ItemParentCategory,
        val itemChildCategory: ItemChildCategory?,
    ): ProductItemPagingSourceData

    object Bookmark: ProductItemPagingSourceData
}

private fun BrandDetailItemResponseData.toDomain() = ProductItemData(
    content = content.map {
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
    totalPages = totalPages,
    last = last,
    number = number,
    first = first
)

private fun FindItemResponseData.toDomain() = ProductItemData(
    content = content.map {
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
    totalPages = totalPages,
    last = last,
    number = number,
    first = first
)

private fun ReadBookmarkItemData.toDomain() = ProductItemData(
    content = content.map {
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
    totalPages = totalPages,
    last = last,
    number = number,
    first = first
)

private fun SearchItemResponseData.toDomain() = ProductItemData(
    content = content.map {
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
    totalPages = totalPages,
    last = last,
    number = number,
    first = first
)