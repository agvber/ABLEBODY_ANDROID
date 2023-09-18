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
import com.example.ablebody_android.data.repository.BrandRepository
import com.example.ablebody_android.data.repository.FindCodyRepository
import com.example.ablebody_android.model.CodyItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CodyItemPagerUseCase @Inject constructor(
    private val brandRepository: BrandRepository,
    private val findCodyRepository: FindCodyRepository
) {

    operator fun invoke(
        brandId: Long,
        gender: List<Gender>,
        category: List<HomeCategory>,
        personHeightRangeStart: Int? = null,
        personHeightRangeEnd: Int? = null,
    ): Flow<PagingData<CodyItemData.Item>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            initialKey = 0
        ) {
            BrandCodyPagingSource(brandId, gender, category, personHeightRangeStart, personHeightRangeEnd)
        }
            .flow

    operator fun invoke(
        gender: List<Gender>,
        category: List<HomeCategory>,
        personHeightRangeStart: Int? = null,
        personHeightRangeEnd: Int? = null,
    ): Flow<PagingData<CodyItemData.Item>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            initialKey = 0
        ) {
            CodyPagingSource(gender, category, personHeightRangeStart, personHeightRangeEnd)
        }
            .flow

    inner class BrandCodyPagingSource(
        private val brandId: Long,
        private val gender: List<Gender>,
        private val category: List<HomeCategory>,
        private val personHeightRangeStart: Int? = null,
        private val personHeightRangeEnd: Int? = null,
    ): PagingSource<Int, CodyItemData.Item>() {
        override fun getRefreshKey(state: PagingState<Int, CodyItemData.Item>): Int? {
            return state.anchorPosition
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CodyItemData.Item> {
            return try {
                val currentPageIndex = params.key ?: 0
                val codyItemData = withContext(Dispatchers.IO) {
                    brandRepository.brandDetailCody(
                        brandId, gender, category, personHeightRangeStart, personHeightRangeEnd, currentPageIndex
                    )
                }
                    .body()?.data?.toDomain()
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

    inner class CodyPagingSource(
        private val gender: List<Gender>,
        private val category: List<HomeCategory>,
        private val personHeightRangeStart: Int? = null,
        private val personHeightRangeEnd: Int? = null,
    ): PagingSource<Int, CodyItemData.Item>() {
        override fun getRefreshKey(state: PagingState<Int, CodyItemData.Item>): Int? {
            return state.anchorPosition
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CodyItemData.Item> {
            return try {
                val currentPageIndex = params.key ?: 0
                val codyItemData = withContext(Dispatchers.IO) {
                    findCodyRepository.findCody(gender, category, personHeightRangeStart, personHeightRangeEnd, currentPageIndex)
                }
                    .body()?.data?.toDomain()
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

private fun BrandDetailCodyResponseData.Item.toDomain() =
    CodyItemData.Item(
        id = id,
        imageURL = imageURL,
        createDate = createDate,
        comments = comments,
        likes = likes,
        views = views,
        isSingleImage = plural
    )

private fun BrandDetailCodyResponseData.toDomain() =
    CodyItemData(
        content = content.map { it.toDomain() },
        totalPages = totalPages,
        last = last,
        number = number,
        first = first
    )

private fun FindCodyResponseData.Item.toDomain() =
    CodyItemData.Item(
        id = id,
        imageURL = imageURL,
        createDate = createDate,
        comments = comments,
        likes = likes,
        views = views,
        isSingleImage = plural
    )

private fun FindCodyResponseData.toDomain() =
    CodyItemData(
        content = content.map { it.toDomain() },
        totalPages = totalPages,
        last = last,
        number = number,
        first = first
    )