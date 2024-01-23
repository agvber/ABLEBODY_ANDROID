package com.smilehunter.ablebody.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.smilehunter.ablebody.data.dto.response.data.GetMyBoardResponseData
import com.smilehunter.ablebody.data.repository.UserRepository
import com.smilehunter.ablebody.model.UserBoardData
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher.IO
import com.smilehunter.ablebody.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserBoardPagerUseCase @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) {

    operator fun invoke(): Flow<PagingData<UserBoardData.Content>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            initialKey = 0
        ) {
            UserBoardPagingSource()
        }
            .flow
    }

    operator fun invoke(uid: String): Flow<PagingData<UserBoardData.Content>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            initialKey = 0
        ) {
            UserBoardPagingSource(uid = uid)
        }
            .flow
    }

    inner class UserBoardPagingSource(
        private val uid: String? = null
    ): PagingSource<Int, UserBoardData.Content>() {
        override fun getRefreshKey(state: PagingState<Int, UserBoardData.Content>): Int? {
            return state.anchorPosition
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserBoardData.Content> =
            try {
                val currentPageIndex = params.key ?: 0

                val userBoardData = withContext(ioDispatcher) {
                    userRepository.getMyBoard(
                        uid = uid,
                        page = currentPageIndex,
                        size = params.loadSize
                    ).data!!.toDomain()
                }

                LoadResult.Page(
                    data = userBoardData.content,
                    prevKey = if (userBoardData.first) null else userBoardData.number - 1,
                    nextKey = if (userBoardData.last) null else userBoardData.number + 1
                )

            } catch (e: Exception) {
                LoadResult.Error(e)
            }

    }
}

private fun GetMyBoardResponseData.toDomain() = UserBoardData(
    content = content.map {
        UserBoardData.Content(
            id = it.id,
            imageURL = it.imageURL,
            createDate = it.createDate,
            comments = it.comments,
            likes = it.likes,
            views = it.views,
            isSingleImage = it.plural,
        )
    },
    totalPages = totalPages,
    last = last,
    number = number,
    first = first
)
