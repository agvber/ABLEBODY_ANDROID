package com.smilehunter.ablebody.presentation.home.bookmark.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.CodyItemData
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.model.ProductItemData
import com.smilehunter.ablebody.model.fake.fakeCodyItemData
import com.smilehunter.ablebody.model.fake.fakeProductItemData
import com.smilehunter.ablebody.presentation.home.bookmark.BookmarkViewModel
import com.smilehunter.ablebody.presentation.main.ui.LocalMainScaffoldPaddingValue
import com.smilehunter.ablebody.presentation.main.ui.error_handler.NetworkConnectionErrorDialog
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.utils.AbleBodyRowTab
import com.smilehunter.ablebody.ui.utils.AbleBodyTabItem
import com.smilehunter.ablebody.ui.utils.ItemSearchBar
import com.smilehunter.ablebody.utils.nonReplyClickable
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun BookmarkListRoute(
    onErrorRequest: (ErrorHandlerCode) -> Unit,
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    productItemClick: (Long) -> Unit,
    codyItemClick: (Long) -> Unit,
    bookmarkViewModel: BookmarkViewModel = hiltViewModel()
) {
    val productPagingItemList = bookmarkViewModel.productPagingItemList.collectAsLazyPagingItems()
    val codyPagingItemList = bookmarkViewModel.codyPagingItemList.collectAsLazyPagingItems()

    BookmarkListScreen(
        onSearchBarClick = onSearchBarClick,
        onAlertButtonClick = onAlertButtonClick,
        productItemClick = productItemClick,
        codyItemClick = codyItemClick,
        requestProductItemDelete = { bookmarkViewModel.deleteProductItem(it) },
        productPagingItemList = productPagingItemList,
        codyPagingItemList = codyPagingItemList,
    )

    var isNetworkDisConnectedDialogShow by remember { mutableStateOf(false) }
    if (isNetworkDisConnectedDialogShow) {
        val context = LocalContext.current
        NetworkConnectionErrorDialog(
            onDismissRequest = {  },
            positiveButtonOnClick = { bookmarkViewModel.refreshNetwork() },
            negativeButtonOnClick = {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                ContextCompat.startActivity(context, intent, null)
            }
        )
    }

    val itemRefreshLoadState = productPagingItemList.loadState.refresh
    val codyRefreshLoadState = codyPagingItemList.loadState.refresh
    if (itemRefreshLoadState is LoadState.Error || codyRefreshLoadState is LoadState.Error) {
        val throwable = when {
            itemRefreshLoadState is LoadState.Error -> itemRefreshLoadState.error
            codyRefreshLoadState is LoadState.Error -> codyRefreshLoadState.error
            else -> return
        }
        val httpException = throwable as? HttpException
        if (httpException?.code() == 404) {
            onErrorRequest(ErrorHandlerCode.NOT_FOUND_ERROR)
            return
        }
        if (httpException != null) {
            onErrorRequest(ErrorHandlerCode.INTERNAL_SERVER_ERROR)
            return
        }
        isNetworkDisConnectedDialogShow = true
    } else {
        if (isNetworkDisConnectedDialogShow) {
            isNetworkDisConnectedDialogShow = false
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookmarkListScreen(
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    productItemClick: (Long) -> Unit,
    codyItemClick: (Long) -> Unit,
    requestProductItemDelete: (List<Long>) -> Unit,
    productPagingItemList: LazyPagingItems<ProductItemData.Item>,
    codyPagingItemList: LazyPagingItems<CodyItemData.Item>,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 2 }
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }
    val productItemRemoveList = remember { mutableStateListOf<Long>() }

    DisposableEffect(key1 = Unit) {
        onDispose { requestProductItemDelete(productItemRemoveList) }
    }

    Scaffold(
        topBar = {
            Column {
                ItemSearchBar(
                    textFiledOnClick = onSearchBarClick,
                    alertOnClick = onAlertButtonClick
                )
                AbleBodyRowTab(
                    selectedTabIndex = currentPage
                ) {
                    AbleBodyTabItem(selected = currentPage == 0, text = "아이템") {
                        scope.launch { pagerState.animateScrollToPage(0) }
                    }
                    AbleBodyTabItem(selected = currentPage == 1, text = "코디") {
                        scope.launch { pagerState.animateScrollToPage(1) }
                    }
                }
            }
        }
    ) { paddingValue ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValue)
        ) { page ->
            when(page) {
                0 -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            count = productPagingItemList.itemCount,
                            key = productPagingItemList.itemKey { it.id }
                        ) { position ->
                            BookmarkProductItemLayout(
                                bookmarkClick = {
                                    productPagingItemList[position]?.id?.let { id ->
                                        if (productItemRemoveList.contains(id)) {
                                            productItemRemoveList.remove(id)
                                        } else {
                                            productItemRemoveList.add(id)
                                        }
                                    }
                                },
                                selected = !productItemRemoveList.contains(productPagingItemList[position]?.id),
                                productName = productPagingItemList[position]?.name ?: "",
                                productPrice = productPagingItemList[position]?.price ?: 0,
                                productSalePrice = productPagingItemList[position]?.salePrice,
                                brandName = productPagingItemList[position]?.brandName ?: "",
                                imageURL = productPagingItemList[position]?.imageURL ?: "",
                                isSingleImage = productPagingItemList[position]?.isSingleImage ?: true,
                                modifier = Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { productPagingItemList[position]?.id?.let(productItemClick) }
                                )
                            )
                        }
                        item(span = { GridItemSpan(2) }) {
                            Box(modifier = Modifier.padding(LocalMainScaffoldPaddingValue.current))
                        }
                    }
                }
                1 -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalArrangement = Arrangement.spacedBy(1.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            count = codyPagingItemList.itemCount,
                            key = codyPagingItemList.itemKey { it.id }
                        ) { position ->
                            Box(
                                modifier = Modifier
                                    .nonReplyClickable { codyPagingItemList[position]?.id?.let(codyItemClick) }
                            ) {
                                AsyncImage(
                                    model = codyPagingItemList[position]?.imageURL,
                                    contentDescription = "cody item",
                                )
                                if (codyPagingItemList[position]?.isSingleImage == false) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_product_item_squaremultiple),
                                        contentDescription = "square multiple",
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(10.dp)
                                    )
                                }
                            }
                        }
                        item(span = { GridItemSpan(3) }) {
                            Box(modifier = Modifier.padding(LocalMainScaffoldPaddingValue.current))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookmarkListScreenPreview() {
    ABLEBODY_AndroidTheme {
        BookmarkListScreen(
            onSearchBarClick = {},
            onAlertButtonClick = {},
            productItemClick = {},
            codyItemClick = {},
            requestProductItemDelete = {},
            productPagingItemList = flowOf(PagingData.from(fakeProductItemData.content)).collectAsLazyPagingItems(),
            codyPagingItemList = flowOf(PagingData.from(fakeCodyItemData.content)).collectAsLazyPagingItems()
        )
    }
}