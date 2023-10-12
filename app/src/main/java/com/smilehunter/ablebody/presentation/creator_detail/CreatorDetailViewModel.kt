package com.smilehunter.ablebody.presentation.creator_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.repository.BookmarkRepository
import com.smilehunter.ablebody.data.repository.CreatorDetailRepository
import com.smilehunter.ablebody.data.repository.OnboardingRepository
import com.smilehunter.ablebody.data.result.Result
import com.smilehunter.ablebody.data.result.asResult
import com.smilehunter.ablebody.domain.GetCreatorDetailDataListUseCase
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import com.smilehunter.ablebody.presentation.creator_detail.data.CreatorDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatorDetailViewModel @Inject constructor(
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    getCreatorDetailDataListUseCase: GetCreatorDetailDataListUseCase,
    onboardingRepository: OnboardingRepository,
    private val creatorDetailRepository: CreatorDetailRepository,
    private val bookmarkRepository: BookmarkRepository
): ViewModel() {

    private val contentID = MutableStateFlow(0L)

    fun updateContentID(id: Long) {
        viewModelScope.launch { contentID.emit(id) }
    }

    private val myUserInfoData = flow {
        emit(onboardingRepository.getUserData().body()?.data)
    }
        .flowOn(ioDispatcher)


    val creatorDetailData: StateFlow<CreatorDetailUiState> =
        contentID.zip(myUserInfoData)  { id, data ->
            getCreatorDetailDataListUseCase(id, data!!.uid)
        }
            .flowOn(ioDispatcher)
            .asResult()
            .map {
                when (it) {
                    is Result.Error -> CreatorDetailUiState.LoadFail
                    is Result.Loading -> CreatorDetailUiState.Loading
                    is Result.Success -> CreatorDetailUiState.Success(it.data)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CreatorDetailUiState.Loading
            )

    fun toggleLike(id: Long) {
        viewModelScope.launch(context = ioDispatcher) { creatorDetailRepository.toggleLike(id) }
    }

    fun toggleBookmark(id: Long) {
        viewModelScope.launch(ioDispatcher) { bookmarkRepository.addBookmarkCody(id) }
    }
}