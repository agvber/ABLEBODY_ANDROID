package com.example.ablebody_android.presentation.bookmark.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.ablebody_android.model.CodyItemData
import com.example.ablebody_android.model.ProductItemData
import com.example.ablebody_android.model.fakeCodyItemData
import com.example.ablebody_android.model.fakeProductItemData
import com.example.ablebody_android.presentation.bookmark.BookmarkViewModel
import com.example.ablebody_android.presentation.main.ui.scaffoldPaddingValueCompositionLocal
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.utils.AbleBodyRowTab
import com.example.ablebody_android.ui.utils.AbleBodyTabItem
import com.example.ablebody_android.ui.utils.ItemSearchBar
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun BookmarkListRoute(
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
        webPageRequest = { /* TODO 웹페이지로 바로가기 or 구매페이지로 바로가기 */ },
        requestProductItemDelete = { bookmarkViewModel.deleteProductItem(it) },
        productPagingItemList = productPagingItemList,
        codyPagingItemList = codyPagingItemList,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookmarkListScreen(
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    productItemClick: (Long) -> Unit,
    codyItemClick: (Long) -> Unit,
    webPageRequest: (String) -> Unit,
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
                        columns = GridCells.Fixed(2)
                    ) {
                        items(
                            count = productPagingItemList.itemCount
                        ) { position ->
                            BookmarkProductItemLayout(
                                requestWebPage = { productPagingItemList[position]?.url?.let(webPageRequest) },
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
                            Box(modifier = Modifier.padding(scaffoldPaddingValueCompositionLocal.current))
                        }
                    }
                }
                1 -> {
                    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                        items(
                            count = codyPagingItemList.itemCount,
                        ) { position ->
                            AsyncImage(
                                model = codyPagingItemList[position]?.imageURL,
                                contentDescription = "cody item",
                                modifier = Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { codyPagingItemList[position]?.id?.let(codyItemClick) }
                                )
                            )
                        }
                        item(span = { GridItemSpan(3) }) {
                            Box(modifier = Modifier.padding(scaffoldPaddingValueCompositionLocal.current))
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
            webPageRequest = { },
            requestProductItemDelete = {},
            productPagingItemList = flowOf(PagingData.from(fakeProductItemData.content)).collectAsLazyPagingItems(),
            codyPagingItemList = flowOf(PagingData.from(fakeCodyItemData.content)).collectAsLazyPagingItems()
        )
    }
}