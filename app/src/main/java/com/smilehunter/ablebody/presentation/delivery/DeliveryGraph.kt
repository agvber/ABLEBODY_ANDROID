package com.smilehunter.ablebody.presentation.delivery

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.smilehunter.ablebody.presentation.delivery.data.DeliveryPassthroughData
import com.smilehunter.ablebody.presentation.delivery.ui.DeliveryRoute
import com.smilehunter.ablebody.presentation.delivery.ui.SearchPostCodeWebView

private enum class DeliveryDestination(val route: String) {
    DELIVERY("delivery"), SEARCH_POST_CODE_WEB_VIEW("search_post_code_web_view")
}

fun NavController.navigateToDeliveryScreen(
    deliveryPassthroughData: DeliveryPassthroughData,
    navOptions: NavOptions? = null
) {
    navigate(
        route = "${DeliveryDestination.DELIVERY.route}?" +
                "attention_name=${deliveryPassthroughData.attentionName}&" +
                "phone_number=${deliveryPassthroughData.phoneNumber}&" +
                "road_address=${deliveryPassthroughData.roadAddress}&" +
                "road_detail_address=${deliveryPassthroughData.roadDetailAddress}&" +
                "zip_code=${deliveryPassthroughData.zipCode}&" +
                "request_message=${deliveryPassthroughData.requestMessage}",
        navOptions = navOptions
    )
}

fun NavController.navigateToSearchPostCodeWebViewScreen(navOptions: NavOptions? = null) {
    navigate(route = DeliveryDestination.SEARCH_POST_CODE_WEB_VIEW.route, navOptions = navOptions)
}

fun NavController.popBackStackForResult(roadAddress: String, zipCode: String) {
    previousBackStackEntry?.savedStateHandle?.set("road_address", roadAddress)
    previousBackStackEntry?.savedStateHandle?.set("zip_code", zipCode)
    popBackStack()
}

fun NavGraphBuilder.deliveryScreen(
    onBackRequest: () -> Unit,
    postCodeRequest: () -> Unit,
    isBottomBarShow: (Boolean) -> Unit
) {
    composable(
        route = "${DeliveryDestination.DELIVERY.route}?" +
                "attention_name={attention_name}&" +
                "phone_number={phone_number}&" +
                "road_address={road_address}&" +
                "road_detail_address={road_detail_address}&" +
                "zip_code={zip_code}&" +
                "request_message={request_message}"
    ) { navBackStackEntry ->
        val attentionNameInitialValue = navBackStackEntry.arguments?.getString("attention_name")?: ""
        val phoneNumberInitialValue = navBackStackEntry.arguments?.getString("phone_number")?: ""
        val roadAddressInitialValue = navBackStackEntry.arguments?.getString("road_address")?: ""
        val roadDetailAddressInitialValue = navBackStackEntry.arguments?.getString("road_detail_address")?: ""
        val zipCodeInitialValue = navBackStackEntry.arguments?.getString("zip_code")?: ""
        val requestMessageInitialValue = navBackStackEntry.arguments?.getString("request_message")?: ""

        val navSavedStateHandle = navBackStackEntry.savedStateHandle
        val customName by navSavedStateHandle.getStateFlow("attention_name", attentionNameInitialValue).collectAsStateWithLifecycle()
        val phoneNumber by navSavedStateHandle.getStateFlow("phone_number", phoneNumberInitialValue).collectAsStateWithLifecycle()
        val roadAddress by navSavedStateHandle.getStateFlow("road_address", roadAddressInitialValue).collectAsStateWithLifecycle()
        val roadDetailAddress by navSavedStateHandle.getStateFlow("road_detail_address", roadDetailAddressInitialValue).collectAsStateWithLifecycle()
        val zipCode by navSavedStateHandle.getStateFlow("zip_code", zipCodeInitialValue).collectAsStateWithLifecycle()
        val requestMessage by navSavedStateHandle.getStateFlow("request_message", requestMessageInitialValue).collectAsStateWithLifecycle()

        DeliveryRoute(
            onBackRequest = onBackRequest,
            postCodeRequest = postCodeRequest,
            customName = customName,
            phoneNumber = phoneNumber,
            roadAddress = roadAddress,
            roadDetailAddress = roadDetailAddress,
            zipCode = zipCode,
            requestMessage = requestMessage
        )
        isBottomBarShow(false)
    }
}

fun NavGraphBuilder.searchPostCodeWebViewScreen(
    onFinished: (String, String) -> Unit,
    isBottomBarShow: (Boolean) -> Unit
) {
    composable(route = DeliveryDestination.SEARCH_POST_CODE_WEB_VIEW.route) {
        SearchPostCodeWebView(onFinished = onFinished)
        isBottomBarShow(false)
    }
}