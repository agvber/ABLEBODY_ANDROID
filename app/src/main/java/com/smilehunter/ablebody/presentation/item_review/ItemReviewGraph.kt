package com.smilehunter.ablebody.presentation.item_review

import android.os.Bundle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.smilehunter.ablebody.model.ItemDetailData
import com.smilehunter.ablebody.presentation.item_review.ui.ItemReviewScreen
import com.smilehunter.ablebody.utils.navigate


const val ItemReviewRoute = "item_review_route"

fun NavController.navigateToItemReview(
    itemReview: ItemDetailData.ItemReview,
    navOptions: NavOptions? = null,
) {
    val bundle = Bundle().apply {
        putParcelable("review_data", itemReview)
    }

    navigate(
        route = ItemReviewRoute,
        args = bundle,
        navOptions = navOptions
    )
}


@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.addItemReviewScreen(
    isBottomShow: (Boolean) -> Unit,
    onBackRequest: () -> Unit,
) {
    composable(
        route = ItemReviewRoute
    ) {
        ItemReviewScreen(
            onBackRequest = onBackRequest
        )
        isBottomShow(false)
    }
}