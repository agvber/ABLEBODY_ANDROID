package com.example.ablebody_android.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ablebody_android.data.dto.Gender
import com.example.ablebody_android.data.dto.HomeCategory
import com.example.ablebody_android.data.dto.response.data.BrandDetailCodyResponseData
import com.example.ablebody_android.data.dto.response.data.FindCodyResponseData
import com.example.ablebody_android.data.dto.response.data.ReadBookmarkCodyData
import com.example.ablebody_android.data.dto.response.data.SearchCodyResponseData
import com.example.ablebody_android.data.repository.BookmarkRepository
import com.example.ablebody_android.data.repository.BrandRepository
import com.example.ablebody_android.data.repository.FindCodyRepository
import com.example.ablebody_android.data.repository.SearchRepository
import com.example.ablebody_android.model.CodyItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CodyItemPagerUseCase @Inject constructor(
    private val brandRepository: BrandRepository,
    private val findCodyRepository: FindCodyRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val searchRepository: SearchRepository
) {
    operator fun invoke(
        codyPagingSourceData: CodyPagingSourceData
    ): Flow<PagingData<CodyItemData.Item>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            initialKey = 0
        ) {
            CodyPagingSource(codyPagingSourceData)
        }
            .flow

    private inner class CodyPagingSource(
        private val codyPagingSourceData: CodyPagingSourceData
    ): PagingSource<Int, CodyItemData.Item>() {
        override fun getRefreshKey(state: PagingState<Int, CodyItemData.Item>): Int? {
            return state.anchorPosition
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CodyItemData.Item> {
            return try {
                val currentPageIndex = params.key ?: 0
                val codyItemData: CodyItemData = withContext(Dispatchers.IO) {
                    when (codyPagingSourceData) {
                        is CodyPagingSourceData.Brand -> {
                            brandRepository.brandDetailCody(
                                codyPagingSourceData.brandId,
                                codyPagingSourceData.gender,
                                codyPagingSourceData.category,
                                codyPagingSourceData.personHeightRangeStart,
                                codyPagingSourceData.personHeightRangeEnd,
                                currentPageIndex
                            )
                                .body()?.data?.toDomain()
                        }
                        is CodyPagingSourceData.CodyRecommended -> {
                            findCodyRepository.findCody(
                                codyPagingSourceData.gender,
                                codyPagingSourceData.category,
                                codyPagingSourceData.personHeightRangeStart,
                                codyPagingSourceData.personHeightRangeEnd,
                                currentPageIndex
                            )
                                .body()?.data?.toDomain()
                        }

                        is CodyPagingSourceData.Search -> {
                            searchRepository.searchCody(
                                codyPagingSourceData.keyword,
                                codyPagingSourceData.genders,
                                codyPagingSourceData.category,
                                codyPagingSourceData.personHeightRangeStart,
                                codyPagingSourceData.personHeightRangeEnd,
                                currentPageIndex
                            )
                                .data?.toDomain()
                        }

                        CodyPagingSourceData.Bookmark -> {
                            bookmarkRepository.readBookmarkCody(currentPageIndex)
                                .body()?.data?.toDomain()
                        }
                    }
                }
                    ?: CodyItemData(emptyList(), 0, true, 0, true)

                LoadResult.Page(
                    data = codyItemData.content,
                    prevKey = if (codyItemData.first) null else codyItemData.number - 1,
                    nextKey = if (codyItemData.last) null else codyItemData.number + 1
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }
}

sealed interface CodyPagingSourceData {
    data class Brand(
        val brandId: Long,
        val gender: List<Gender>,
        val category: List<HomeCategory>,
        val personHeightRangeStart: Int? = null,
        val personHeightRangeEnd: Int? = null,
    ): CodyPagingSourceData
    data class CodyRecommended(
        val gender: List<Gender>,
        val category: List<HomeCategory>,
        val personHeightRangeStart: Int? = null,
        val personHeightRangeEnd: Int? = null,
    ): CodyPagingSourceData
    data class Search(
        val keyword: String,
        val genders: List<Gender>,
        val category: List<HomeCategory>,
        val personHeightRangeStart: Int? = null,
        val personHeightRangeEnd: Int? = null,
    ): CodyPagingSourceData
    object Bookmark: CodyPagingSourceData
}

private fun BrandDetailCodyResponseData.toDomain() =
    CodyItemData(
        content = content.map {
            CodyItemData.Item(
                id = it.id,
                imageURL = it.imageURL,
                createDate = it.createDate,
                comments = it.comments,
                likes = it.likes,
                views = it.views,
                isSingleImage = it.plural
            )
        },
        totalPages = totalPages,
        last = last,
        number = number,
        first = first
    )
private fun FindCodyResponseData.toDomain() =
    CodyItemData(
        content = content.map {
            CodyItemData.Item(
                id = it.id,
                imageURL = it.imageURL,
                createDate = it.createDate,
                comments = it.comments,
                likes = it.likes,
                views = it.views,
                isSingleImage = it.plural
            )
        },
        totalPages = totalPages,
        last = last,
        number = number,
        first = first
    )
private fun ReadBookmarkCodyData.toDomain() =
    CodyItemData(
        content = content.map {
            CodyItemData.Item(
                id = it.id,
                imageURL = it.imageURL,
                createDate = it.createDate,
                comments = it.comments,
                likes = it.likes,
                views = it.views,
                isSingleImage = it.plural
            )
        },
        totalPages = totalPages,
        last = last,
        number = number,
        first = first
    )
private fun SearchCodyResponseData.toDomain() =
    CodyItemData(
        content = content.map {
            CodyItemData.Item(
                id = it.id,
                imageURL = it.imageURL,
                createDate = it.createDate,
                comments = it.comments,
                likes = it.likes,
                views = it.views,
                isSingleImage = it.plural
            )
        },
        totalPages = totalPages,
        last = last,
        number = number,
        first = first
    )