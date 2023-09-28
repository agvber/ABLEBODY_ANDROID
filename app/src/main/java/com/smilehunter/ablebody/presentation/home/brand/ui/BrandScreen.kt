package com.smilehunter.ablebody.presentation.home.brand.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.data.dto.response.data.BrandMainResponseData
import com.smilehunter.ablebody.data.result.Result
import com.smilehunter.ablebody.presentation.home.brand.BrandViewModel
import com.smilehunter.ablebody.presentation.main.ui.scaffoldPaddingValueCompositionLocal
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.DefaultFilterTabItem
import com.smilehunter.ablebody.ui.utils.DefaultFilterTabRow
import com.smilehunter.ablebody.ui.utils.DropDownFilterLayout
import com.smilehunter.ablebody.ui.utils.ItemSearchBar
import com.smilehunter.ablebody.ui.utils.ProductItemFilterBottomSheet
import com.smilehunter.ablebody.ui.utils.ProductItemFilterBottomSheetItem
import com.smilehunter.ablebody.ui.utils.previewPlaceHolder
import kotlinx.coroutines.launch

@Composable
fun BrandRoute(
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    onItemClick: (Long, String) -> Unit,
    viewModel: BrandViewModel = hiltViewModel()
) {
    val sortingMethod by viewModel.brandListSortingMethod.collectAsStateWithLifecycle()
    val genderFilter by viewModel.brandListGenderFilterType.collectAsStateWithLifecycle()
    val brandItemList by viewModel.brandItemList.collectAsStateWithLifecycle()

    BrandScreen(
        onSearchBarClick = onSearchBarClick,
        onAlertButtonClick = onAlertButtonClick,
        sortingMethod = sortingMethod,
        onSortingMethodChange = { viewModel.updateBrandListOrderFilterType(it) },
        genderFilter = genderFilter,
        onGenderFilterChange = { viewModel.updateBrandListGenderFilterType(it) },
        brandItemList = brandItemList,
        onItemClick = onItemClick
    )
    if (brandItemList is Result.Error) {
        // TODO: Error Handling
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BrandScreen(
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    sortingMethod: SortingMethod,
    onSortingMethodChange: (SortingMethod) -> Unit,
    genderFilter: ItemGender,
    onGenderFilterChange: (ItemGender) -> Unit,
    brandItemList: Result<List<BrandMainResponseData>>,
    onItemClick: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFilterBottomSheetShow by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isFilterBottomSheetShow) {
            ProductItemFilterBottomSheet(onDismissRequest = { isFilterBottomSheetShow = false }) {
                items(items = SortingMethod.values()) { item ->
                    ProductItemFilterBottomSheetItem(
                        sheetState = sheetState,
                        value = item.string,
                        onValueChange = {
                            onSortingMethodChange(item)
                            scope.launch { lazyListState.animateScrollToItem(0) }
                            isFilterBottomSheetShow = false
                        }
                    )
                }
            }
        }
        Scaffold(
            modifier = modifier,
            topBar = {
                ItemSearchBar(
                    textFiledOnClick = onSearchBarClick,
                    alertOnClick = onAlertButtonClick
                )
            }
        ) { paddingValue ->
            Column(
                modifier = Modifier.padding(paddingValue)
            ) {
                BrandFilterTab(
                    genderFilter = genderFilter,
                    onGenderFilterChange = {
                        onGenderFilterChange(it)
                        scope.launch { lazyListState.animateScrollToItem(0) }
                    },
                    sortingMethod = sortingMethod,
                    onSortingMethodChange = { isFilterBottomSheetShow = true }
                )
                if (brandItemList is Result.Success) {
                    LazyColumn(
                        state = lazyListState
                    ) {
                        items(
                            items = brandItemList.data,
                            key = { it.id }
                        ) {
                            BrandListItemLayout(
                                modifier = Modifier.animateItemPlacement(),
                                brandName = it.name,
                                subName = it.subName,
                                thumbnailURL = it.thumbnail,
                                maxDisCountString = it.maxDiscount,
                                onClick = { onItemClick(it.id, it.name) }
                            )
                            Divider(
                                thickness = 1.dp,
                                startIndent = 1.dp,
                                modifier = Modifier.height(1.dp)
                            )
                        }
                        item {
                            Box(modifier = Modifier.padding(scaffoldPaddingValueCompositionLocal.current))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BrandScreenPreview() {
    ABLEBODY_AndroidTheme {
        BrandScreen(
            onSearchBarClick = {},
            onAlertButtonClick = {},
            sortingMethod = SortingMethod.POPULAR,
            onSortingMethodChange = {},
            genderFilter = ItemGender.UNISEX,
            onGenderFilterChange = {},
            brandItemList = Result.Success(listOf(
                BrandMainResponseData(
                    name = "NIKE",
                    id = 3,
                    thumbnail = "",
                    subName = "나이키",
                    brandGender = ItemGender.UNISEX,
                    maxDiscount = 0
                ),
                BrandMainResponseData(
                    name = "Positive Me",
                    id = 36,
                    thumbnail = "",
                    subName = "포지티브미",
                    brandGender = ItemGender.FEMALE,
                    maxDiscount = 0
                ),
                BrandMainResponseData(
                    name = "MAVRK",
                    id = 30,
                    thumbnail = "",
                    subName = "매버릭",
                    brandGender = ItemGender.MALE,
                    maxDiscount = 46
                ),
                BrandMainResponseData(
                    name = "adidas",
                    id = 1,
                    thumbnail = "",
                    subName = "아디다스",
                    brandGender = ItemGender.UNISEX,
                    maxDiscount = 0
                )
            )),
            onItemClick = { id, name -> }
        )
    }
}

@Composable
fun BrandListItemLayout(
    modifier: Modifier = Modifier,
    brandName: String,
    subName: String,
    thumbnailURL: Any?,
    maxDisCountString: Int,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(86.dp)
            .padding(horizontal = 25.dp, vertical = 15.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        val (brandImage, brandMainName, brandSubName, discountText, chevronButton) = createRefs()

        AsyncImage(
            model = thumbnailURL,
            contentDescription = "brand image",
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 4.dp, bottom = 4.dp, end = 15.dp)
                .shadow(3.dp, shape = CircleShape)
                .constrainAs(ref = brandImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
            ,
            placeholder = previewPlaceHolder(id = R.drawable.brand_test)
        )

        Text(
            text = brandName,
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight(500),
            ),
            modifier = Modifier.constrainAs(brandMainName) {
                top.linkTo(brandImage.top)
                bottom.linkTo(brandSubName.top)
                absoluteLeft.linkTo(brandImage.absoluteRight)
            }
        )

        Text(
            text = subName,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight(500),
                color = SmallTextGrey,
            ),
            modifier = Modifier.constrainAs(brandSubName) {
                top.linkTo(brandMainName.bottom)
                bottom.linkTo(brandImage.bottom)
                absoluteLeft.linkTo(brandImage.absoluteRight)
            }
        )

        if (maxDisCountString != 0) {
            Text(
                text = "최대 $maxDisCountString% 할인 중",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight(500),
                    color = AbleBlue,
                ),
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .constrainAs(discountText) {
                        top.linkTo(brandMainName.bottom)
                        bottom.linkTo(brandImage.bottom)
                        absoluteLeft.linkTo(brandSubName.absoluteRight)
                    }
            )
        }

        Image(
            painter = painterResource(id = R.drawable.chevronforward),
            contentDescription = "chevronForwardButton",
            modifier = Modifier.constrainAs(ref = chevronButton) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                absoluteRight.linkTo(parent.absoluteRight)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BrandListItemLayoutPreview() {
    BrandListItemLayout(
        brandName = "제이엘브",
        subName = "JELEVE",
        thumbnailURL = "https://",
        maxDisCountString = 51,
        onClick = {  }
    )
}

@Composable
fun BrandFilterTab(
    sortingMethod: SortingMethod,
    onSortingMethodChange: () -> Unit,
    genderFilter: ItemGender,
    onGenderFilterChange: (ItemGender) -> Unit,
) {
    DefaultFilterTabRow(
        actionContent = {
            DropDownFilterLayout(
                value = sortingMethod.string,
                onClick = onSortingMethodChange
            )
        }
    ) {
        ItemGender.values().forEach {
            DefaultFilterTabItem(
                selected = genderFilter == it,
                text = it.string,
                onClick = { onGenderFilterChange(it) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BrandFilterTabPreview(
    sortingMethod: SortingMethod = SortingMethod.POPULAR,
    onSortingMethodChange: () -> Unit = {},
    genderFilter: ItemGender = ItemGender.UNISEX,
    onGenderFilterChange: (ItemGender) -> Unit = {},
) {
    BrandFilterTab(
        sortingMethod = sortingMethod,
        onSortingMethodChange = onSortingMethodChange,
        genderFilter = genderFilter,
        onGenderFilterChange = onGenderFilterChange,
    )
}