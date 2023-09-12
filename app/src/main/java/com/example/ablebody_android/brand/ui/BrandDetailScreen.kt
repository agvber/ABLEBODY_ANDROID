package com.example.ablebody_android.brand.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
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
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.SmallTextGrey
import com.example.ablebody_android.ui.theme.White
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
        onBackClick = onBackClick,
        contentName = contentName,
        productItemSortingMethod = brandViewModel.brandProductItemSortingMethod.collectAsStateWithLifecycle().value,
        onProductItemSortingMethodChange = { brandViewModel.updateBrandProductItemOrderFilterType(it) },
        productItemParentFilter = brandViewModel.brandProductItemParentFilter.collectAsStateWithLifecycle().value,
        onProductItemParentFilterChange = { brandViewModel.updateBrandProductItemParentFilter(it) },
        productItemChildCategory = brandViewModel.brandProductItemChildCategory.collectAsStateWithLifecycle().value,
        productItemChildFilter = brandViewModel.brandProductItemChildFilter.collectAsStateWithLifecycle().value,
        onProductItemChildFilterChange = { brandViewModel.updateBrandProductItemChildFilter(it) },
        productItemGender = brandViewModel.brandProductItemGender.collectAsStateWithLifecycle().value,
        onProductItemGenderChange = { brandViewModel.updateBrandProductItemGender(it) },
        productContentItem = productItemContentList,
        productItemLoadNextOnPageChangeListener = { brandViewModel.requestProductItemListPage() },
        codyItemFilterResetRequest = { brandViewModel.resetCodyItemListFilter() },
        codyItemListGenderFilterList = brandViewModel.codyItemListGenderFilter.collectAsStateWithLifecycle().value,
        onCodyItemListGenderFilterChange = { brandViewModel.updateCodyItemListGendersFilter(it) },
        codyItemListSportFilter = brandViewModel.codyItemListSportFilter.collectAsStateWithLifecycle().value,
        onCodyItemListSportFilterChange = { brandViewModel.updateCodyItemListSportFilter(it) },
        codyItemListPersonHeightFilter = brandViewModel.codyItemListPersonHeightFilter.collectAsStateWithLifecycle().value,
        onCodyItemListPersonHeightFilterChange = { brandViewModel.updateCodyItemListPersonHeightFilter(it) },
        codyItemContentList = codyItemContentList,
        codyItemLoadNextOnPageChangeListener = { brandViewModel.requestCodyItemPageChange() },
        modifier = modifier
    )
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrandDetailScreen(
    onBackClick: () -> Unit,
    contentName: String,
    productItemSortingMethod: SortingMethod,
    onProductItemSortingMethodChange: (SortingMethod) -> Unit,
    productItemParentFilter: ItemParentCategory,
    onProductItemParentFilterChange: (ItemParentCategory) -> Unit,
    productItemChildCategory: List<ItemChildCategory>,
    productItemChildFilter: ItemChildCategory?,
    onProductItemChildFilterChange: (ItemChildCategory?) -> Unit,
    productItemGender: ItemGender,
    onProductItemGenderChange: (ItemGender) -> Unit,
    productContentItem: List<BrandDetailItemResponseData.Item>,
    productItemLoadNextOnPageChangeListener: () -> Unit,
    codyItemFilterResetRequest: () -> Unit,
    codyItemListGenderFilterList: List<Gender>,
    onCodyItemListGenderFilterChange: (List<Gender>) -> Unit,
    codyItemListSportFilter: List<HomeCategory>,
    onCodyItemListSportFilterChange: (List<HomeCategory>) -> Unit,
    codyItemListPersonHeightFilter: PersonHeightFilterType,
    onCodyItemListPersonHeightFilterChange: (PersonHeightFilterType) -> Unit,
    codyItemContentList: List<BrandDetailCodyResponseData.Item>,
    codyItemLoadNextOnPageChangeListener: () -> Unit,
    modifier: Modifier = Modifier,
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
fun BrandDetailScreenPreview(
    onBackClick: () -> Unit = {},
    contentName: String = "",
    productItemSortingMethod: SortingMethod = SortingMethod.POPULAR,
    onProductItemSortingMethodChange: (SortingMethod) -> Unit = {},
    productItemParentFilter: ItemParentCategory = ItemParentCategory.ALL,
    onProductItemParentFilterChange: (ItemParentCategory) -> Unit = {},
    productItemChildCategory: List<ItemChildCategory> = ItemChildCategory.values().toList(),
    productItemChildFilter: ItemChildCategory? = null,
    onProductItemChildFilterChange: (ItemChildCategory?) -> Unit = {},
    productItemGender: ItemGender = ItemGender.UNISEX,
    onProductItemGenderChange: (ItemGender) -> Unit = {},
    productContentItemList: List<BrandDetailItemResponseData.Item> = fakeBrandDetailItemResponseData.content,
    productItemLoadNextOnPageChangeListener: () -> Unit = {},
    codyItemFilterResetRequest: () -> Unit = {},
    codyItemListGenderFilterList: List<Gender> = listOf(),
    onCodyItemListGenderFilterChange: (List<Gender>) -> Unit = {},
    codyItemListSportFilter: List<HomeCategory> = listOf(),
    onCodyItemListSportFilterChange: (List<HomeCategory>) -> Unit = {},
    codyItemListPersonHeightFilter: PersonHeightFilterType = PersonHeightFilterType.ALL,
    onCodyItemListPersonHeightFilterChange: (PersonHeightFilterType) -> Unit = {},
    codyContentItemList: List<BrandDetailCodyResponseData.Item> = fakeBrandDetailCodyResponseData.content,
    codyItemLoadNextOnPageChangeListener: () -> Unit = {},
) {
    ABLEBODY_AndroidTheme {
        BrandDetailScreen(
            onBackClick = onBackClick,
            contentName = contentName,
            productItemSortingMethod = productItemSortingMethod,
            onProductItemSortingMethodChange = onProductItemSortingMethodChange,
            productItemParentFilter = productItemParentFilter,
            onProductItemParentFilterChange = onProductItemParentFilterChange,
            productItemChildCategory = productItemChildCategory,
            productItemChildFilter = productItemChildFilter,
            onProductItemChildFilterChange = onProductItemChildFilterChange,
            productItemGender = productItemGender,
            onProductItemGenderChange = onProductItemGenderChange,
            productContentItem = productContentItemList,
            productItemLoadNextOnPageChangeListener = productItemLoadNextOnPageChangeListener,
            codyItemFilterResetRequest = codyItemFilterResetRequest,
            codyItemListGenderFilterList = codyItemListGenderFilterList,
            onCodyItemListGenderFilterChange = onCodyItemListGenderFilterChange,
            codyItemListSportFilter = codyItemListSportFilter,
            onCodyItemListSportFilterChange = onCodyItemListSportFilterChange,
            codyItemListPersonHeightFilter = codyItemListPersonHeightFilter,
            onCodyItemListPersonHeightFilterChange = onCodyItemListPersonHeightFilterChange,
            codyItemContentList = codyContentItemList,
            codyItemLoadNextOnPageChangeListener = { codyItemLoadNextOnPageChangeListener() }
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
        TabRow(
            selectedTabIndex = selectedTabIndex,
            contentColor = AbleBlue,
            indicator = @Composable { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = AbleBlue
                )
            },
        ) {
            TabTextLayout(
                selected = selectedTabIndex == 0,
                text = "아이템",
                onclick = { tabOnClick(0) }
            )
            TabTextLayout(
                selected = selectedTabIndex == 1,
                text = "코디",
                onclick = { tabOnClick(1) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BrandDetailTopBarLayoutPreview() {
    var state by remember { mutableIntStateOf(0) }
    ABLEBODY_AndroidTheme {
        BrandDetailTopBarLayout(
            titleText = "오옴",
            backButtonClicked = { /*TODO*/ },
            selectedTabIndex = state,
            tabOnClick = { state = it }
        )
    }
}

@Composable
private fun TabTextLayout(
    selected: Boolean,
    text: String,
    onclick: () -> Unit
) {
    val textColor by animateColorAsState(
        targetValue = if (selected) AbleBlue else SmallTextGrey
    )
    val textWeight by animateIntAsState(
        targetValue = if (selected) 500 else 400
    )
    val fontResourceID = if (selected) R.font.noto_sans_cjk_kr_regular else R.font.noto_sans_cjk_kr_medium
    Text(
        text = text,
        style = TextStyle(
            fontSize = 15.sp,
            fontFamily = FontFamily(Font(fontResourceID)),
            fontWeight = FontWeight(textWeight),
            color = textColor,
            textAlign = TextAlign.Center,
        ),
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onclick
        )
    )
}