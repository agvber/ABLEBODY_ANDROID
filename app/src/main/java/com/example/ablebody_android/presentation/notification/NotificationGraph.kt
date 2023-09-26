package com.example.ablebody_android.presentation.notification

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ablebody_android.presentation.notification.ui.NotificationRoute

internal const val NotificationRoute = "NotificationRoute"
fun NavGraphBuilder.addNotificationScreen(
    onBackRequest: () -> Unit,
    itemClick: (Long) -> Unit,
) {
    composable(route = "NotificationRoute") {
        NotificationRoute(onBackRequest = onBackRequest, itemClick = itemClick)
    }
}