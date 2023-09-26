package com.example.ablebody_android.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ablebody_android.data.dto.response.data.GetMyNotiResponseData
import com.example.ablebody_android.data.repository.NotificationRepository
import com.example.ablebody_android.model.NotificationItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationItemPagerUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {

    operator fun invoke(): Flow<PagingData<NotificationItemData.Content>> {
        return Pager(
            config =  PagingConfig(pageSize = 20),
            initialKey = 0,
            pagingSourceFactory = { NotificationPagingSource() }
        )
            .flow
    }

    private inner class NotificationPagingSource: PagingSource<Int, NotificationItemData.Content>() {
        override fun getRefreshKey(state: PagingState<Int, NotificationItemData.Content>): Int? =
            state.anchorPosition

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationItemData.Content> =
            try {
                val currentPageIndex = params.key ?: 0
                val itemData: NotificationItemData = withContext(Dispatchers.IO) {
                    notificationRepository.getMyNoti(page = currentPageIndex).data!!.toDomain()
                }
                LoadResult.Page(
                    data = itemData.content,
                    prevKey = if (itemData.first) null else itemData.number - 1,
                    nextKey = if (itemData.last) null else itemData.number + 1
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
    }
}

private fun GetMyNotiResponseData.toDomain() = NotificationItemData(
    content = content.map {
      NotificationItemData.Content(
          id = it.id,
          senderProfileImageURL = it.from.profileUrl,
          senderNickname = it.from.nickname,
          createDate = it.createDate,
          text = it.content,
          uri = it.url,
          checked = it.checked,
      )
    },
    totalPages = totalPages,
    last = last,
    number = number,
    first = first
)