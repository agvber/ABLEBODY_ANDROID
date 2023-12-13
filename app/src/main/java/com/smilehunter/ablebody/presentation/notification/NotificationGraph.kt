package com.smilehunter.ablebody.presentation.notification

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.notification.ui.NotificationRoute

internal const val NotificationRoute = "NotificationRoute"
fun NavGraphBuilder.addNotificationScreen(
    isBottomBarShow: (Boolean) -> Unit,
    onErrorRequest: (ErrorHandlerCode) -> Unit,
    onBackRequest: () -> Unit,
    itemClick: (String) -> Unit,
) {
    composable(route = "NotificationRoute") {
        NotificationRoute(
            onErrorRequest = onErrorRequest,
            onBackRequest = onBackRequest,
            itemClick = itemClick
        )
        isBottomBarShow(true)
    }
}