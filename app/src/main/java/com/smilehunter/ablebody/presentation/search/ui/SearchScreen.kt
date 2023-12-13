package com.smilehunter.ablebody.presentation.search.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.model.SearchHistoryQuery
import com.smilehunter.ablebody.presentation.main.ui.error_handler.NetworkConnectionErrorDialog
import com.smilehunter.ablebody.presentation.search.SearchViewModel
import com.smilehunter.ablebody.presentation.search.data.KeywordUiState
import com.smilehunter.ablebody.ui.cody_item.CodyItemListLayout
import com.smilehunter.ablebody.ui.product_item.ProductItemListLayout
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.AbleBodyRowTab
import com.smilehunter.ablebody.ui.utils.AbleBodyTabItem
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun SearchRoute(
    onErrorOccur: (ErrorHandlerCode) -> Unit,
    backRequest: () -> Unit,
    productItemClick: (Long) -> Unit,
    codyItemClick: (Long) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    SearchScreen(
        onErrorOccur = onErrorOccur,
        backRequest = backRequest,
        productItemClick = productItemClick,
        codyItemClick = codyItemClick,
        searchViewModel = searchViewModel
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    onErrorOccur: (ErrorHandlerCode) -> Unit,
    backRequest: () -> Unit,
    productItemClick: (Long) -> Unit,
    codyItemClick: (Long) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })

    val keyword by searchViewModel.keyword.collectAsStateWithLifecycle()
    val searchHistoryQueries by searchViewModel.searchHistoryQueries.collectAsStateWithLifecycle()
    val recommendedKeywords by searchViewModel.recommendedKeywords.collectAsStateWithLifecycle()

    val productPagingItemList = searchViewModel.productPagingItemList.collectAsLazyPagingItems()
    val codyPagingItemList = searchViewModel.codyPagingItemList.collectAsLazyPagingItems()
    Column {
        SearchScreenTopBar(
            backRequest = backRequest,
            resetRequest = { searchViewModel.updateKeyword("") },
            value = keyword,
            onValueChange = { searchViewModel.updateKeyword(it) }
        )
        AnimatedVisibility(visible = keyword.isEmpty()) {
            if (recommendedKeywords is KeywordUiState.RecommendKeyword) {
                SearchKeywordLayout(
                    searchHistoryResetRequest = { searchViewModel.deleteAllSearchHistory() },
                    selectText = { searchViewModel.updateKeyword(it) },
                    searchHistoryQueries = searchHistoryQueries,
                    recommendedKeywords = (recommendedKeywords as KeywordUiState.RecommendKeyword).data
                )
            }
        }
        AnimatedVisibility(
            visible = keyword.isNotEmpty(),
        ) {
            Column {
                AbleBodyRowTab(
                    selectedTabIndex = pagerState.currentPage
                ) {
                    AbleBodyTabItem(selected = pagerState.currentPage == 0, text = "아이템") {
                        scope.launch { pagerState.animateScrollToPage(0) }
                    }
                    AbleBodyTabItem(selected = pagerState.currentPage == 1, text = "코디") {
                        scope.launch { pagerState.animateScrollToPage(1) }
                    }
                }
                HorizontalPager(
                    state = pagerState
                ) { page ->
                    when(page) {
                        0 -> ProductItemListLayout(
                            itemClick = productItemClick,
                            onSortingMethodChange = { searchViewModel.updateProductItemSortingMethod(it) },
                            onParentFilterChange = { searchViewModel.updateProductItemParentFilter(it) },
                            onChildFilterChange = { searchViewModel.updateBrandProductItemChildFilter(it) },
                            onGenderChange = { searchViewModel.updateProductItemGender(it) },
                            sortingMethod = searchViewModel.productItemSortingMethod.collectAsStateWithLifecycle().value,
                            itemParentCategory = searchViewModel.productItemParentCategory.collectAsStateWithLifecycle().value,
                            itemChildCategory = searchViewModel.productItemChildCategory.collectAsStateWithLifecycle().value,
                            gender = searchViewModel.productItemGender.collectAsStateWithLifecycle().value,
                            productPagingItems = productPagingItemList
                        )
                        1 -> CodyItemListLayout(
                            itemClick = codyItemClick,
                            resetRequest = { searchViewModel.resetCodyItemFilter() },
                            onCodyItemListGenderFilterChange = { searchViewModel.updateCodyItemListGenders(it) },
                            onCodyItemListSportFilterChange = { searchViewModel.updateCodyItemListSportFilter(it) },
                            onCodyItemListPersonHeightFilterChange = { searchViewModel.updateCodyItemListPersonHeightFilter(it) },
                            codyItemListGenderFilterList = searchViewModel.codyItemListGenderFilter.collectAsStateWithLifecycle().value,
                            codyItemListSportFilter = searchViewModel.codyItemListSportFilter.collectAsStateWithLifecycle().value,
                            codyItemListPersonHeightFilter = searchViewModel.codyItemListPersonHeightFilter.collectAsStateWithLifecycle().value,
                            codyItemData = codyPagingItemList
                        )
                    }
                }
            }
        }
    }

    var isNetworkDisConnectedDialogShow by remember { mutableStateOf(false) }
    if (isNetworkDisConnectedDialogShow) {
        val context = LocalContext.current
        NetworkConnectionErrorDialog(
            onDismissRequest = {  },
            positiveButtonOnClick = { searchViewModel.refreshNetwork() },
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
            onErrorOccur(ErrorHandlerCode.NOT_FOUND_ERROR)
            return
        }
        if (httpException != null) {
            onErrorOccur(ErrorHandlerCode.INTERNAL_SERVER_ERROR)
            return
        }
        isNetworkDisConnectedDialogShow = true
    } else {
        if (isNetworkDisConnectedDialogShow) {
            isNetworkDisConnectedDialogShow = false
        }
    }
}

@Composable
private fun SearchKeywordLayout(
    searchHistoryResetRequest: () -> Unit,
    selectText: (String) -> Unit,
    searchHistoryQueries: List<SearchHistoryQuery>,
    recommendedKeywords: List<String>
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        AnimatedVisibility(visible = searchHistoryQueries.isNotEmpty()) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "최근 검색어",
                        style = TextStyle(
                            fontSize = 18.sp,
                            lineHeight = 26.sp,
                            fontWeight = FontWeight(700),
                            color = AbleDark,
                        ),
                        modifier = Modifier.padding(vertical = 15.dp)
                    )
                    Box(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = searchHistoryResetRequest
                        )
                    ) {
                        Text(
                            text = "모두 지우기",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight(400),
                                color = Color(0xFF8C959E),
                            )
                        )
                    }
                }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = searchHistoryQueries) {
                        RoundedCornerButton(onClick = { selectText(it.query) }) {
                            Text(
                                text = it.query,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight(400),
                                    color = AbleDark,
                                    textAlign = TextAlign.Center,
                                )
                            )
                        }
                    }
                }
            }
        }

        Column {
            Text(
                text = "추천 검색어",
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight(700),
                    color = AbleDark,
                ),
                modifier = Modifier.padding(vertical = 15.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = recommendedKeywords) {
                    RoundedCornerButton(onClick = { selectText(it) }) {
                        Text(
                            text = it,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight(400),
                                color = AbleDark,
                                textAlign = TextAlign.Center,
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchScreenTopBar(
    backRequest: () -> Unit,
    resetRequest: () -> Unit,
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 10.dp, horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.backward),
            contentDescription = "back",
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { backRequest() /* TODO keyboard hidden? */ }
            )
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .height(45.dp)
                .weight(1f)
                .focusRequester(focusRequester),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "브랜드, 제품명 등을 검색해 보세요",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight(400),
                                color = SmallTextGrey,
                            )
                        )
                    }
                    innerTextField()
                }
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight(400),
                color = AbleDark,
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                }
            ),
            cursorBrush = Brush.verticalGradient(
                0.00f to Color.Transparent,
                0.10f to Color.Transparent,
                0.10f to AbleBlue,
                0.90f to AbleBlue,
                0.90f to Color.Transparent,
                1.00f to Color.Transparent
            )
        )
        if (value.isNotEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.ic_circlexmark),
                contentDescription = "clear",
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = resetRequest
                )
            )
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun RoundedCornerButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable (BoxScope.() -> Unit)
) {
    Card(
        shape = RoundedCornerShape(size = 100.dp),
        backgroundColor = PlaneGrey,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenTopBarPreview() {
    SearchScreenTopBar(backRequest = {}, resetRequest = {}, value = "", onValueChange = {})
}
@Preview(showBackground = true)
@Composable
private fun SearchKeywordLayoutPreview() {
    SearchKeywordLayout(
        searchHistoryResetRequest = {},
        selectText = {},
        searchHistoryQueries = listOf(SearchHistoryQuery("가위", 0L)),
        recommendedKeywords = listOf("나이키", "애블바디", "가나다")
    )
}