package com.example.ablebody_android.brand.ui

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ablebody_android.Gender
import com.example.ablebody_android.HomeCategory
import com.example.ablebody_android.ItemChildCategory
import com.example.ablebody_android.ItemGender
import com.example.ablebody_android.ItemParentCategory
import com.example.ablebody_android.PersonHeightFilterType
import com.example.ablebody_android.R
import com.example.ablebody_android.SortingMethod
import com.example.ablebody_android.brand.BrandViewModel
import com.example.ablebody_android.brand.data.fakeBrandDetailCodyResponseData
import com.example.ablebody_android.brand.data.fakeBrandDetailItemResponseData
import com.example.ablebody_android.retrofit.dto.response.data.BrandDetailCodyResponseData
import com.example.ablebody_android.retrofit.dto.response.data.BrandDetailItemResponseData
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
    brandViewModel: BrandViewModel = viewModel(factory = BrandViewModel.Factory)
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
        productItemChildCategory = brandViewModel.brandProductItemChildCategory.collectAsStateWithLifecycle().value,
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
    productItemChildCategory: List<ItemChildCategory> = ItemChildCategory.values().toList(),
    productItemChildFilter: ItemChildCategory? = null,
    productItemGender: ItemGender = ItemGender.UNISEX,
    productContentItem: List<BrandDetailItemResponseData.Item>,
    codyItemListGenderFilterList: List<Gender> = listOf(),
    codyItemListSportFilter: List<HomeCategory> = listOf(),
    onCodyItemListSportFilterChange: (List<HomeCategory>) -> Unit = {},
    codyItemListPersonHeightFilter: PersonHeightFilterType = PersonHeightFilterType.ALL,
    codyItemContentList: List<BrandDetailCodyResponseData.Item>,
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
                    0 -> BrandProductItemListLayout(
                            sortingMethod = productItemSortingMethod,
                            onSortingMethodChange = { onProductItemSortingMethodChange(it) },
                            parentFilter = productItemParentFilter,
                            onParentFilterChange = { onProductItemParentFilterChange(it) },
                            itemChildCategory = productItemChildCategory,
                            childFilter = productItemChildFilter,
                            onChildFilterChange = { onProductItemChildFilterChange(it) },
                            gender = productItemGender,
                            onGenderChange = { onProductItemGenderChange(it) },
                            productContentItem = productContentItem,
                            loadNextOnPageChangeListener = productItemLoadNextOnPageChangeListener
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
            productContentItem = fakeBrandDetailItemResponseData.content,
            codyItemContentList = fakeBrandDetailCodyResponseData.content
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