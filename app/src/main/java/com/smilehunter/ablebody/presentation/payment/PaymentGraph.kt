package com.smilehunter.ablebody.presentation.payment

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.delivery.data.DeliveryPassthroughData
import com.smilehunter.ablebody.presentation.payment.data.PaymentPassthroughData
import com.smilehunter.ablebody.presentation.payment.ui.PaymentRoute
import com.smilehunter.ablebody.utils.navigate

const val PaymentRoute = "payment_route"

private enum class PaymentDestination(val route: String) {
    PAYMENT("payment")
}

fun NavController.navigateToPayment(
    paymentPassthroughData: PaymentPassthroughData,
    navOptions: NavOptions? = null
) {
    val bundle = Bundle().apply {
        putParcelable("payment_passthrough_data", paymentPassthroughData)
    }

    navigate(
        route = PaymentRoute,
        args = bundle,
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
        route = PaymentRoute,
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