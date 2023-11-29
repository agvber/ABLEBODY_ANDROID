package com.smilehunter.ablebody.presentation.home.my

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.dto.request.EditProfile
import com.smilehunter.ablebody.data.result.Result
import com.smilehunter.ablebody.data.result.asResult
import com.smilehunter.ablebody.domain.CheckNicknameUseCase
import com.smilehunter.ablebody.domain.EditProfileUseCase
import com.smilehunter.ablebody.domain.GetUserInfoUseCase
import com.smilehunter.ablebody.domain.Status
import com.smilehunter.ablebody.presentation.home.my.data.EditProfileUiStatus
import com.smilehunter.ablebody.presentation.home.my.data.NicknameCheckUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val editProfileUseCase: EditProfileUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val checkNicknameUseCase: CheckNicknameUseCase
): ViewModel() {

    val userInfoData = flow { emit(getUserInfoUseCase()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val _uploadStatus = MutableStateFlow<EditProfileUiStatus>(EditProfileUiStatus.Loading)
    val uploadStatus = _uploadStatus.asStateFlow()

    fun changeProfile(
        inputStream: InputStream?,
        defaultProfileImageNumber: Int?
    ) {
        viewModelScope.launch {
            _uploadStatus.emit(EditProfileUiStatus.Uploading)
            try {
                val status = editProfileUseCase(
                    editProfile = EditProfile(
                        nickname = nickname.value,
                        name = userName.value,
                        height = userHeight.value.toIntOrNull(),
                        weight = userWeight.value.toIntOrNull(),
                        job = userJob.value,
                        introduction = introduction.value,
                        defaultProfileImage = defaultProfileImageNumber
                    ),
                    profileImageInputStream = inputStream
                )
                when (status) {
                    Status.SUCCESS -> _uploadStatus.emit(EditProfileUiStatus.Success)
                    Status.FAIL -> _uploadStatus.emit(EditProfileUiStatus.LoadFail(null))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uploadStatus.emit(EditProfileUiStatus.LoadFail(e))
            }
        }
    }

    private val _nickname = MutableStateFlow("")
    val nickname = callbackFlow<String> {
        val scope = viewModelScope.launch {
            userInfoData.collectLatest { send(it?.nickname ?: "") }
        }
        _nickname.collectLatest { send(it) }
        awaitClose { scope.cancel() }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ""
        )

    fun updateNickname(value: String) {
        viewModelScope.launch { _nickname.emit(value) }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val isNicknameAvailable: StateFlow<NicknameCheckUiState> =
        nickname.flatMapLatest {
            if (it.isEmpty() || it == userInfoData.value?.nickname) {
                return@flatMapLatest flowOf(null)
            }
            flowOf(checkNicknameUseCase(it))
        }
            .asResult()
            .map {
                when (it) {
                    is Result.Error -> NicknameCheckUiState.LoadFail(it.exception)
                    is Result.Loading -> NicknameCheckUiState.Loading
                    is Result.Success -> {
                        if (it.data == null) {
                            return@map NicknameCheckUiState.Loading
                        }
                        if (it.data) {
                            return@map NicknameCheckUiState.Available
                        }
                        NicknameCheckUiState.UnAvailable
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = NicknameCheckUiState.Loading
            )



    private val _userName = MutableStateFlow("")
    val userName = callbackFlow<String> {
        val scope = viewModelScope.launch {
            userInfoData.collectLatest { send(it?.name ?: "") }
        }
        _userName.collectLatest { send(it) }
        awaitClose { scope.cancel() }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ""
        )

    fun updateUserName(value: String) {
        viewModelScope.launch { _userName.emit(value) }
    }

    private val _userHeight = MutableStateFlow("")
    val userHeight = callbackFlow<String> {
        val scope = viewModelScope.launch {
            userInfoData.collectLatest { send(it?.height?.toString() ?: "") }
        }
        _userHeight.collectLatest { send(it) }
        awaitClose { scope.cancel() }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ""
        )

    fun updateUserHeight(value: String) {
        viewModelScope.launch {
            if (value.isDigitsOnly()) {
                _userHeight.emit(value)
            }
        }
    }

    private val _userWeight = MutableStateFlow("")
    val userWeight = callbackFlow<String> {
        val scope = viewModelScope.launch {
            userInfoData.collectLatest { send(it?.weight?.toString() ?: "") }
        }
        _userWeight.collectLatest { send(it) }
        awaitClose { scope.cancel() }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ""
        )

    fun updateUserWeight(value: String) {
        viewModelScope.launch {
            if (value.isDigitsOnly()) {
                _userWeight.emit(value)
            }
        }
    }

    private val _userJob = MutableStateFlow("")
    val userJob = callbackFlow<String> {
        val scope = viewModelScope.launch {
            userInfoData.collectLatest { send(it?.job ?: "") }
        }
        _userJob.collectLatest { send(it) }
        awaitClose { scope.cancel() }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ""
        )

    fun updateUserJob(value: String) {
        viewModelScope.launch { _userJob.emit(value) }
    }

    private val _introduction = MutableStateFlow("")
    val introduction = callbackFlow<String> {
        val scope = viewModelScope.launch {
            userInfoData.collectLatest { send(it?.introduction ?: "") }
        }
        _introduction.collectLatest { send(it) }
        awaitClose { scope.cancel() }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ""
        )

    fun updateIntroduction(value: String) {
        viewModelScope.launch {
            if (value.length <= 34) {
                _introduction.emit(value)
            }
        }
    }
}