package com.smilehunter.ablebody.presentation.my.report

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.dto.request.ReportRequest
import com.smilehunter.ablebody.data.repository.ManageRepository
import com.smilehunter.ablebody.data.repository.UserRepository
import com.smilehunter.ablebody.domain.AddCouponUseCase
import com.smilehunter.ablebody.domain.GetCouponListUseCase
import com.smilehunter.ablebody.domain.GetOrderItemListUseCase
import com.smilehunter.ablebody.domain.GetUserBoardPagerUseCase
import com.smilehunter.ablebody.domain.GetUserInfoUseCase
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val manageRepository: ManageRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getCouponListUseCase: GetCouponListUseCase,
    private val getOrderItemListUseCase: GetOrderItemListUseCase,
    private val getUserBoardPagerUseCase: GetUserBoardPagerUseCase,
    private val addCouponUseCase: AddCouponUseCase,
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
//    fun sendSuggest(value: String) {
//        viewModelScope.launch(ioDispatcher) {
//            userRepository.suggestApp(value)
//            Log.d("sendSuggest", value)
//        }
//    }
//    fun reportUser(reportRequest: ReportRequest){
//        viewModelScope.launch(ioDispatcher) {
//            manageRepository.report(reportRequest)
//        }
//    }
}