package com.example.ablebody_android.bookmark.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ablebody_android.bookmark.BookmarkViewModel
import com.example.ablebody_android.data.dto.response.data.ReadBookmarkCodyData
import com.example.ablebody_android.data.dto.response.data.ReadBookmarkItemData
import com.example.ablebody_android.main.ui.scaffoldPaddingValueCompositionLocal
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.utils.AbleBodyRowTab
import com.example.ablebody_android.ui.utils.AbleBodyTabItem
import com.example.ablebody_android.ui.utils.InfiniteVerticalGrid
import com.example.ablebody_android.ui.utils.ItemSearchBar
import kotlinx.coroutines.launch

@Composable
fun BookmarkListRoute(bookmarkViewModel: BookmarkViewModel = hiltViewModel()) {
    val bookmarkProductItemList by bookmarkViewModel.productItemList.collectAsStateWithLifecycle()
    val codyItemList by bookmarkViewModel.codyItemList.collectAsStateWithLifecycle()

    BookmarkListScreen(
        requestProductItemDelete = { bookmarkViewModel.deleteProductItem(it) },
        productItemList = bookmarkProductItemList,
        productItemLoadNextOnPageChangeListener = { bookmarkViewModel.requestProductItemPageChange() },
        codyItemList = codyItemList,
        codyItemLoadNextOnPageChangeListener = { bookmarkViewModel.requestCodyItemPageChange() }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookmarkListScreen(
    requestProductItemDelete: (List<Long>) -> Unit,
    productItemList: List<ReadBookmarkItemData.Item>,
    productItemLoadNextOnPageChangeListener: () -> Unit,
    codyItemList: List<ReadBookmarkCodyData.Item>,
    codyItemLoadNextOnPageChangeListener: () -> Unit,
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
                ItemSearchBar(textFiledOnClick = { /*TODO searchBar*/ }) { }
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
                    InfiniteVerticalGrid(
                        lastPositionListener = productItemLoadNextOnPageChangeListener,
                        columns = GridCells.Fixed(2)
                    ) {
                        items(productItemList) { item ->
                            BookmarkProductItemLayout(
                                requestWebPage = { /*TODO*/ },
                                bookmarkClick = {
                                    if (productItemRemoveList.contains(item.id)) {
                                        productItemRemoveList.remove(item.id)
                                    } else {
                                        productItemRemoveList.add(item.id)
                                    }
                                                },
                                selected = !productItemRemoveList.contains(item.id),
                                productName = item.name,
                                productPrice = item.price,
                                productSalePrice = item.salePrice,
                                brandName = item.brandName,
                                imageURL = item.image,
                                isSingleImage = item.isPlural,
                            )
                        }
                        item(span = { GridItemSpan(2) }) {
                            Box(modifier = Modifier.padding(scaffoldPaddingValueCompositionLocal.current))
                        }
                    }
                }
                1 -> {
                    InfiniteVerticalGrid(
                        lastPositionListener = codyItemLoadNextOnPageChangeListener,
                        columns = GridCells.Fixed(3)
                    ) {
                        items(codyItemList) { item ->
                            AsyncImage(
                                model = item.imageURL,
                                contentDescription = "cody item"
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
            {},
            productItemList = listOf(ReadBookmarkItemData.Item(id = 0L, name = "만두", price = 99999999, salePrice = null, brandName = "만두", image = "", isPlural = false, url = "", avgStarRating = null)),
            productItemLoadNextOnPageChangeListener = {  },
            codyItemList = emptyList()
        ) {

        }
    }
}