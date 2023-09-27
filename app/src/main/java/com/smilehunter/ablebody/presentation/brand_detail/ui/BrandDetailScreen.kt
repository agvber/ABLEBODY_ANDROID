package com.smilehunter.ablebody.presentation.brand_detail.ui

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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.HomeCategory
import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.PersonHeightFilterType
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.model.CodyItemData
import com.smilehunter.ablebody.model.ProductItemData
import com.smilehunter.ablebody.model.fake.fakeCodyItemData
import com.smilehunter.ablebody.model.fake.fakeProductItemData
import com.smilehunter.ablebody.presentation.brand_detail.BrandDetailViewModel
import com.smilehunter.ablebody.ui.cody_item.CodyItemListLayout
import com.smilehunter.ablebody.ui.product_item.ProductItemListLayout
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.White
import com.smilehunter.ablebody.ui.utils.AbleBodyRowTab
import com.smilehunter.ablebody.ui.utils.AbleBodyTabItem
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun BrandDetailRoute(
    onBackClick: () -> Unit,
    contentID: Long?,
    contentName: String,
    modifier: Modifier = Modifier,
    brandDetailViewModel: BrandDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) { contentID?.let { brandDetailViewModel.updateContentID(it) } }

    BrandDetailScreen(
        modifier = modifier,
        onBackClick = onBackClick,
        productItemClick = { /* TODO 아이템 버튼 클릭 */ },
        onProductItemSortingMethodChange = { brandDetailViewModel.updateBrandProductItemOrderFilterType(it) },
        onProductItemParentFilterChange = { brandDetailViewModel.updateBrandProductItemParentFilter(it) },
        onProductItemChildFilterChange = { brandDetailViewModel.updateBrandProductItemChildFilter(it) },
        onProductItemGenderChange = { brandDetailViewModel.updateBrandProductItemGender(it) },
        codyItemClick = { /* TODO 코디 아이템 버튼 클릭 */ },
        codyItemFilterResetRequest = { brandDetailViewModel.resetCodyItemFilter() },
        onCodyItemListGenderFilterChange = { brandDetailViewModel.updateCodyItemListGendersFilter(it) },
        onCodyItemListSportFilterChange = { brandDetailViewModel.updateCodyItemListSportFilter(it) },
        onCodyItemListPersonHeightFilterChange = { brandDetailViewModel.updateCodyItemListPersonHeightFilter(it) },
        contentName = contentName,
        productItemSortingMethod = brandDetailViewModel.brandProductItemSortingMethod.collectAsStateWithLifecycle().value,
        productItemParentFilter = brandDetailViewModel.brandProductItemParentFilter.collectAsStateWithLifecycle().value,
        productItemChildFilter = brandDetailViewModel.brandProductItemChildFilter.collectAsStateWithLifecycle().value,
        productItemGender = brandDetailViewModel.brandProductItemGender.collectAsStateWithLifecycle().value,
        productPagingItems = brandDetailViewModel.productItemContentList.collectAsLazyPagingItems(),
        codyItemListGenderFilterList = brandDetailViewModel.codyItemListGenderFilter.collectAsStateWithLifecycle().value,
        codyItemListSportFilter = brandDetailViewModel.codyItemListSportFilter.collectAsStateWithLifecycle().value,
        codyItemListPersonHeightFilter = brandDetailViewModel.codyItemListPersonHeightFilter.collectAsStateWithLifecycle().value,
        codyPagingItem = brandDetailViewModel.codyPagingItem.collectAsLazyPagingItems()
    )
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrandDetailScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    productItemClick: (Long) -> Unit = {},
    onProductItemSortingMethodChange: (SortingMethod) -> Unit = {},
    onProductItemParentFilterChange: (ItemParentCategory) -> Unit = {},
    onProductItemChildFilterChange: (ItemChildCategory?) -> Unit = {},
    onProductItemGenderChange: (ItemGender) -> Unit = {},
    codyItemClick: (Long) -> Unit = {} ,
    codyItemFilterResetRequest: () -> Unit = {},
    onCodyItemListGenderFilterChange: (List<Gender>) -> Unit = {},
    onCodyItemListSportFilterChange: (List<HomeCategory>) -> Unit = {},
    onCodyItemListPersonHeightFilterChange: (PersonHeightFilterType) -> Unit = {},
    contentName: String = "",
    productItemSortingMethod: SortingMethod = SortingMethod.POPULAR,
    productItemParentFilter: ItemParentCategory = ItemParentCategory.ALL,
    productItemChildFilter: ItemChildCategory? = null,
    productItemGender: ItemGender = ItemGender.UNISEX,
    productPagingItems: LazyPagingItems<ProductItemData.Item>,
    codyItemListGenderFilterList: List<Gender> = listOf(),
    codyItemListSportFilter: List<HomeCategory> = listOf(),
    codyItemListPersonHeightFilter: PersonHeightFilterType = PersonHeightFilterType.ALL,
    codyPagingItem: LazyPagingItems<CodyItemData.Item>
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
                        itemClick = productItemClick,
                        onSortingMethodChange = onProductItemSortingMethodChange,
                        onParentFilterChange = onProductItemParentFilterChange,
                        onChildFilterChange = onProductItemChildFilterChange,
                        onGenderChange = onProductItemGenderChange,
                        sortingMethod = productItemSortingMethod,
                        itemParentCategory = productItemParentFilter,
                        itemChildCategory = productItemChildFilter,
                        gender = productItemGender,
                        productPagingItems = productPagingItems
                    )
                    1 -> CodyItemListLayout(
                        itemClick = codyItemClick,
                        resetRequest = codyItemFilterResetRequest,
                        onCodyItemListGenderFilterChange = onCodyItemListGenderFilterChange,
                        onCodyItemListSportFilterChange = onCodyItemListSportFilterChange,
                        onCodyItemListPersonHeightFilterChange = onCodyItemListPersonHeightFilterChange,
                        codyItemListGenderFilterList = codyItemListGenderFilterList,
                        codyItemListSportFilter = codyItemListSportFilter,
                        codyItemListPersonHeightFilter = codyItemListPersonHeightFilter,
                        codyItemData = codyPagingItem
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
            productPagingItems = flowOf(PagingData.from(fakeProductItemData.content)).collectAsLazyPagingItems(),
            codyPagingItem = flowOf(PagingData.from(fakeCodyItemData.content)).collectAsLazyPagingItems()
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