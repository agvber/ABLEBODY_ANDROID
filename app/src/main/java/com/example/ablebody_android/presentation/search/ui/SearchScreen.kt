package com.example.ablebody_android.presentation.search.ui

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
import androidx.compose.material.TextField
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ablebody_android.R
import com.example.ablebody_android.data.result.Result
import com.example.ablebody_android.database.model.SearchHistoryEntity
import com.example.ablebody_android.presentation.search.SearchViewModel
import com.example.ablebody_android.ui.cody_item.CodyItemListLayout
import com.example.ablebody_android.ui.product_item.ProductItemListLayout
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.PlaneGrey
import com.example.ablebody_android.ui.theme.SmallTextGrey
import com.example.ablebody_android.ui.utils.AbleBodyRowTab
import com.example.ablebody_android.ui.utils.AbleBodyTabItem
import kotlinx.coroutines.launch

@Composable
fun SearchRoute(
    backRequest: () -> Unit,
    productItemClick: (Long) -> Unit,
    codyItemClick: (Long) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    SearchScreen(
        backRequest = backRequest,
        productItemClick = productItemClick,
        codyItemClick = codyItemClick,
        searchViewModel = searchViewModel
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
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
    Column {
        SearchScreenTopBar(
            backRequest = backRequest,
            resetRequest = { searchViewModel.updateKeyword("") },
            value = keyword,
            onValueChange = { searchViewModel.updateKeyword(it) }
        )
        AnimatedVisibility(visible = keyword.isEmpty()) {
            SearchKeywordLayout(
                searchHistoryResetRequest = { searchViewModel.deleteAllSearchHistory() },
                selectText = { searchViewModel.updateKeyword(it) },
                searchHistoryQueries = searchHistoryQueries,
                recommendedKeywords = recommendedKeywords
            )
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
                            productPagingItems = searchViewModel.productPagingItemList.collectAsLazyPagingItems()
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
                            codyItemData = searchViewModel.codyPagingItemList.collectAsLazyPagingItems()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchKeywordLayout(
    searchHistoryResetRequest: () -> Unit,
    selectText: (String) -> Unit,
    searchHistoryQueries: List<SearchHistoryEntity>,
    recommendedKeywords: Result<List<String>>
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
            if (recommendedKeywords is Result.Success) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = recommendedKeywords.data) {
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
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
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
        searchHistoryQueries = listOf(SearchHistoryEntity("가위", 0L)),
        recommendedKeywords = Result.Success(listOf("나이키", "애블바디", "가나다"))
    )
}

@Preview(showBackground = true)
@Composable
fun Testw() {

    var status by remember { mutableStateOf("") }

    TextField(
        value = status,
        onValueChange = { status = it },
        placeholder = { Text(text = "AAAAAAAAAAAAAAAAAAAA") }
    )

}