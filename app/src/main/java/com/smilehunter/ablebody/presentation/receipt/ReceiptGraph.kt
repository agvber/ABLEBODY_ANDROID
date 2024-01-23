package com.smilehunter.ablebody.presentation.receipt

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.payment.PaymentRoute
import com.smilehunter.ablebody.presentation.receipt.ui.ReceiptRoute

const val ReceiptRoute = "receipt_route"

fun NavController.navigateToReceiptScreen(
    id: String,
    navOptions: NavOptions? = androidx.navigation.navOptions { popUpTo(PaymentRoute) { inclusive = true } },
) {
    navigate("$ReceiptRoute/$id", navOptions)
}

fun NavGraphBuilder.addReceiptScreen(
    onErrorOccur: (ErrorHandlerCode) -> Unit,
    orderComplete: () -> Unit,
) {
    composable("receipt_route/{content_id}") {
        ReceiptRoute(
            onErrorOccur = onErrorOccur,
            orderComplete = orderComplete
        )
    }
}