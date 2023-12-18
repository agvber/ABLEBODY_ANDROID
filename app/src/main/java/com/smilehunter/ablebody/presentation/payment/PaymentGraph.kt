package com.smilehunter.ablebody.presentation.payment

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.gson.Gson
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.delivery.data.DeliveryPassthroughData
import com.smilehunter.ablebody.presentation.payment.data.PaymentPassthroughData
import com.smilehunter.ablebody.presentation.payment.ui.PaymentRoute

const val PaymentRoute = "payment_route"

private enum class PaymentDestination(val route: String) {
    PAYMENT("payment")
}

fun NavController.navigateToPayment(
    paymentPassthroughData: PaymentPassthroughData,
    navOptions: NavOptions? = null
) {
    navigate(
        route = "$PaymentRoute?" +
                "payment_passthrough_data=${Gson().toJson(paymentPassthroughData)}",
        navOptions = navOptions
    )
}


fun NavGraphBuilder.addPaymentGraph(
    isBottomBarShow: (Boolean) -> Unit,
    onErrorOccur: (ErrorHandlerCode) -> Unit,
    onBackRequest: () -> Unit,
    addressRequest: (DeliveryPassthroughData) -> Unit,
    receiptRequest: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        startDestination = PaymentDestination.PAYMENT.route,
        route = "$PaymentRoute?" +
                "payment_passthrough_data={payment_passthrough_data}",
    ) {

        composable(route = PaymentDestination.PAYMENT.route) {

            PaymentRoute(
                onErrorOccur = onErrorOccur,
                onBackRequest = onBackRequest,
                addressRequest = addressRequest,
                receiptRequest = receiptRequest
            )
            isBottomBarShow(false)
        }
        nestedGraphs()
    }
}