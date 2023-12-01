package com.smilehunter.ablebody.presentation.order_management

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.order_management.ui.OrderItemDetailRoute
import com.smilehunter.ablebody.presentation.order_management.ui.OrderItemListRoute

const val OrderManagementRoute = "order_management_route"

enum class OrderManagementDestination(val route: String) {
    ORDER_ITEM_LIST("order_item_list_route"),
    ORDER_ITEM_DETAIL("order_item_detail_route")
}

fun NavController.navigateToOrderManagementGraph(
    navOptions: NavOptions? = null
) {
    navigate(route = OrderManagementRoute, navOptions = navOptions)
}

fun NavController.navigateToOrderItemDetailScreen(
    id: String,
    navOptions: NavOptions? = null
) {
    navigate(route = "${OrderManagementDestination.ORDER_ITEM_DETAIL.route}/$id", navOptions = navOptions)
}


fun NavGraphBuilder.addOrderManagementGraph(
    onBackRequest: () -> Unit,
    itemOnClick: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
    isBottomBarShow: (Boolean) -> Unit,
) {
    navigation(
        startDestination = OrderManagementDestination.ORDER_ITEM_LIST.route,
        route = OrderManagementRoute
    ) {
        composable(
            route = OrderManagementDestination.ORDER_ITEM_LIST.route
        ) {
            OrderItemListRoute(
                onBackRequest = onBackRequest,
                itemOnClick = itemOnClick
            )
            isBottomBarShow(false)
        }
        nestedGraphs()
    }
}

fun NavGraphBuilder.addOrderItemDetailScreen(
    isBottomBarShow: (Boolean) -> Unit,
    onErrorRequest: (ErrorHandlerCode) -> Unit,
    onBackRequest: () -> Unit,
) {
    composable(
        route = "${OrderManagementDestination.ORDER_ITEM_DETAIL.route}/{content_id}"
    ) {
        OrderItemDetailRoute(
            onErrorRequest = onErrorRequest,
            onBackRequest = onBackRequest
        )
        isBottomBarShow(false)
    }
}
