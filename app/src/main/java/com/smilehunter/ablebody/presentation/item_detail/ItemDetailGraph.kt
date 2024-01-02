package com.smilehunter.ablebody.presentation.item_detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.model.ItemDetailData
import com.smilehunter.ablebody.presentation.item_detail.ui.ItemDetailRoute
import com.smilehunter.ablebody.presentation.payment.data.PaymentPassthroughData

const val ItemDetailGraph = "item_detail_graph"
const val ItemDetailRoute = "item_detail_route"

fun NavController.navigateToItemDetailGraph(
    contentId: Long,
    navOptions: NavOptions? = null
) {
    navigate("$ItemDetailGraph?content_id=$contentId", navOptions)
}

fun NavGraphBuilder.addItemDetailGraph(
    isBottomBarShow: (Boolean) -> Unit,
    onErrorOccur: (ErrorHandlerCode) -> Unit,
    onBackRequest: () -> Unit,
    brandOnClick: (Long, String) -> Unit,
    codyOnClick: (Long) -> Unit,
    itemReview: (ItemDetailData.ItemReview) -> Unit,
    purchaseOnClick: (PaymentPassthroughData) -> Unit,
    nestedGraph: NavGraphBuilder.() -> Unit
) {

    navigation(
        startDestination = ItemDetailRoute,
        route = "$ItemDetailGraph?" +
                "content_id={content_id}",
        listOf(
            navArgument("content_id") { this.type = NavType.LongType }
        )
    ) {

        composable(
            route = ItemDetailRoute,
        ) {
            ItemDetailRoute(
                onErrorOccur = onErrorOccur,
                onBackRequest = onBackRequest,
                purchaseOnClick = purchaseOnClick,
                itemClick = itemReview,
                brandOnClick = brandOnClick,
                codyOnClick = codyOnClick
            )
            isBottomBarShow(false)
        }
        nestedGraph()
    }
}