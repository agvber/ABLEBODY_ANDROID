package com.smilehunter.ablebody.presentation.home.bookmark.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.smilehunter.ablebody.model.CodyItemData
import com.smilehunter.ablebody.model.ProductItemData
import com.smilehunter.ablebody.model.fake.fakeCodyItemData
import com.smilehunter.ablebody.model.fake.fakeProductItemData
import com.smilehunter.ablebody.presentation.home.bookmark.BookmarkViewModel
import com.smilehunter.ablebody.presentation.main.ui.scaffoldPaddingValueCompositionLocal
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.utils.AbleBodyRowTab
import com.smilehunter.ablebody.ui.utils.AbleBodyTabItem
import com.smilehunter.ablebody.ui.utils.ItemSearchBar
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
                            Box(modifier = Modifier.padding(scaffoldPaddingValueCompositionLocal.current))
                        }
                    }
                }
                1 -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            count = codyPagingItemList.itemCount,
                            key = codyPagingItemList.itemKey { it.id }
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
            requestProductItemDelete = {},
            productPagingItemList = flowOf(PagingData.from(fakeProductItemData.content)).collectAsLazyPagingItems(),
            codyPagingItemList = flowOf(PagingData.from(fakeCodyItemData.content)).collectAsLazyPagingItems()
        )
    }
}