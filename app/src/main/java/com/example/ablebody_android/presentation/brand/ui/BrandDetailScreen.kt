package com.example.ablebody_android.presentation.brand.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ablebody_android.R
import com.example.ablebody_android.presentation.brand.BrandViewModel
import com.example.ablebody_android.model.fakeProductItemData
import com.example.ablebody_android.data.dto.Gender
import com.example.ablebody_android.data.dto.HomeCategory
import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.PersonHeightFilterType
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.model.ProductItemData
import com.example.ablebody_android.ui.product_item.ProductItemListLayout
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.White
import com.example.ablebody_android.ui.utils.AbleBodyRowTab
import com.example.ablebody_android.ui.utils.AbleBodyTabItem
import kotlinx.coroutines.launch

@Composable
fun BrandDetailRoute(
    onBackClick: () -> Unit,
    contentID: Long?,
    contentName: String,
    modifier: Modifier = Modifier,
    brandViewModel: BrandViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) { contentID?.let { brandViewModel.updateContentID(it) } }

    val productItemContentList by brandViewModel.productItemContentList.collectAsStateWithLifecycle()
    val codyItemContentList by brandViewModel.codyItemContentList.collectAsStateWithLifecycle()

    BrandDetailScreen(
        modifier = modifier,
        onBackClick = onBackClick,
        productItemLoadNextOnPageChangeListener = { brandViewModel.requestProductItemListPage() },
        onProductItemSortingMethodChange = { brandViewModel.updateBrandProductItemOrderFilterType(it) },
        onProductItemParentFilterChange = { brandViewModel.updateBrandProductItemParentFilter(it) },
        onProductItemChildFilterChange = { brandViewModel.updateBrandProductItemChildFilter(it) },
        onProductItemGenderChange = { brandViewModel.updateBrandProductItemGender(it) },
        codyItemFilterResetRequest = { brandViewModel.resetCodyItemListFilter() },
        codyItemLoadNextOnPageChangeListener = { brandViewModel.requestCodyItemPageChange() },
        onCodyItemListGenderFilterChange = { brandViewModel.updateCodyItemListGendersFilter(it) },
        onCodyItemListPersonHeightFilterChange = { brandViewModel.updateCodyItemListPersonHeightFilter(it) },
        contentName = contentName,
        productItemSortingMethod = brandViewModel.brandProductItemSortingMethod.collectAsStateWithLifecycle().value,
        productItemParentFilter = brandViewModel.brandProductItemParentFilter.collectAsStateWithLifecycle().value,
        productItemChildFilter = brandViewModel.brandProductItemChildFilter.collectAsStateWithLifecycle().value,
        productItemGender = brandViewModel.brandProductItemGender.collectAsStateWithLifecycle().value,
        productContentItem = productItemContentList,
        codyItemListGenderFilterList = brandViewModel.codyItemListGenderFilter.collectAsStateWithLifecycle().value,
        codyItemListSportFilter = brandViewModel.codyItemListSportFilter.collectAsStateWithLifecycle().value,
        onCodyItemListSportFilterChange = { brandViewModel.updateCodyItemListSportFilter(it) },
        codyItemListPersonHeightFilter = brandViewModel.codyItemListPersonHeightFilter.collectAsStateWithLifecycle().value,
        codyItemContentList = codyItemContentList
    )
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrandDetailScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    productItemLoadNextOnPageChangeListener: () -> Unit = {},
    onProductItemSortingMethodChange: (SortingMethod) -> Unit = {},
    onProductItemParentFilterChange: (ItemParentCategory) -> Unit = {},
    onProductItemChildFilterChange: (ItemChildCategory?) -> Unit = {},
    onProductItemGenderChange: (ItemGender) -> Unit = {},
    codyItemFilterResetRequest: () -> Unit = {},
    codyItemLoadNextOnPageChangeListener: () -> Unit = {},
    onCodyItemListGenderFilterChange: (List<Gender>) -> Unit = {},
    onCodyItemListPersonHeightFilterChange: (PersonHeightFilterType) -> Unit = {},
    contentName: String = "",
    productItemSortingMethod: SortingMethod = SortingMethod.POPULAR,
    productItemParentFilter: ItemParentCategory = ItemParentCategory.ALL,
    productItemChildFilter: ItemChildCategory? = null,
    productItemGender: ItemGender = ItemGender.UNISEX,
    productContentItem: List<ProductItemData.Item>,
    codyItemListGenderFilterList: List<Gender> = listOf(),
    codyItemListSportFilter: List<HomeCategory> = listOf(),
    onCodyItemListSportFilterChange: (List<HomeCategory>) -> Unit = {},
    codyItemListPersonHeightFilter: PersonHeightFilterType = PersonHeightFilterType.ALL,
    codyItemContentList: List<com.example.ablebody_android.data.dto.response.data.BrandDetailCodyResponseData.Item>,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })

    Scaffold(
        modifier = modifier,
        topBar = {
            BrandDetailTopBarLayout(
                titleText = contentName,
                backButtonClicked = onBackClick,
                selectedTabIndex = pagerState.currentPage,
                tabOnClick = { scope.launch { pagerState.animateScrollToPage(it) } }
            )
        },
        content = { paddingValue ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(paddingValue)
            ) { page ->
                when(page) {
                    0 -> ProductItemListLayout(
                        itemClick = {},
                        requestNextPage = productItemLoadNextOnPageChangeListener,
                        productContentItem = productContentItem,
                        onSortingMethodChange = onProductItemSortingMethodChange,
                        onParentFilterChange = onProductItemParentFilterChange,
                        onChildFilterChange = onProductItemChildFilterChange,
                        onGenderChange = onProductItemGenderChange,
                        sortingMethod = productItemSortingMethod,
                        itemParentCategory = productItemParentFilter,
                        itemChildCategory = productItemChildFilter,
                        gender = productItemGender
                    )
                    1 -> BrandCodyItemListLayout(
                        resetRequest = codyItemFilterResetRequest,
                        codyItemListGenderFilterList = codyItemListGenderFilterList,
                        onCodyItemListGenderFilterChange = onCodyItemListGenderFilterChange,
                        codyItemListSportFilter = codyItemListSportFilter,
                        onCodyItemListSportFilterChange = onCodyItemListSportFilterChange,
                        codyItemListPersonHeightFilter = codyItemListPersonHeightFilter,
                        onCodyItemListPersonHeightFilterChange = onCodyItemListPersonHeightFilterChange,
                        codyItemContentList = codyItemContentList,
                        loadNextOnPageChangeListener = codyItemLoadNextOnPageChangeListener
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BrandDetailScreenPreview() {
    ABLEBODY_AndroidTheme {
        BrandDetailScreen(
            productContentItem = fakeProductItemData.content,
            codyItemContentList = emptyList()
        )
    }
}

@Composable
fun BrandDetailTopBarLayout(
    titleText: String,
    backButtonClicked: () -> Unit,
    selectedTabIndex: Int,
    tabOnClick: (Int) -> Unit
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = titleText,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(400),
                        color = AbleDark,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            },
            navigationIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_appbar_back_button),
                    contentDescription = "back button",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = backButtonClicked
                        )
                )
            },
            backgroundColor = White,
        )
        AbleBodyRowTab(
            selectedTabIndex = selectedTabIndex
        ) {
            AbleBodyTabItem(selected = selectedTabIndex == 0, text = "아이템") {
                tabOnClick(0)
            }
            AbleBodyTabItem(selected = selectedTabIndex == 1, text = "코디") {
                tabOnClick(1)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BrandDetailTopBarLayoutPreview() {
    ABLEBODY_AndroidTheme {
        BrandDetailTopBarLayout(
            titleText = "",
            backButtonClicked = {  },
            selectedTabIndex = 0,
            tabOnClick = {  }
        )
    }
}