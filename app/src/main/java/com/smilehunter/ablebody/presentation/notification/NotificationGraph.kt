package com.smilehunter.ablebody.presentation.notification

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilehunter.ablebody.presentation.notification.ui.NotificationRoute

internal const val NotificationRoute = "NotificationRoute"
fun NavGraphBuilder.addNotificationScreen(
    isBottomBarShow: (Boolean) -> Unit,
    onBackRequest: () -> Unit,
    itemClick: (Long) -> Unit,
) {
    composable(route = "NotificationRoute") {
        NotificationRoute(onBackRequest = onBackRequest, itemClick = itemClick)
        isBottomBarShow(true)
    }
}