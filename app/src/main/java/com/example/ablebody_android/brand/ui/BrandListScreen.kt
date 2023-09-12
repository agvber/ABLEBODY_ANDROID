package com.example.ablebody_android.brand.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ablebody_android.ItemGender
import com.example.ablebody_android.R
import com.example.ablebody_android.SortingMethod
import com.example.ablebody_android.brand.BrandViewModel
import com.example.ablebody_android.main.ui.scaffoldPaddingValueCompositionLocal
import com.example.ablebody_android.retrofit.dto.response.data.BrandMainResponseData
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.SmallTextGrey
import com.example.ablebody_android.ui.utils.DefaultFilterTabItem
import com.example.ablebody_android.ui.utils.DefaultFilterTabRow
import com.example.ablebody_android.ui.utils.DropDownFilterLayout
import com.example.ablebody_android.ui.utils.ProductItemFilterBottomSheet
import com.example.ablebody_android.ui.utils.ProductItemFilterBottomSheetItem
import com.example.ablebody_android.ui.utils.previewPlaceHolder
import com.example.ablebody_android.utils.ItemSearchBar

@Composable
fun BrandListRoute(
    onItemClick: (Long, String) -> Unit,
    viewModel: BrandViewModel = viewModel(factory = BrandViewModel.Factory),
) {
    val sortingMethod by viewModel.brandListSortingMethod.collectAsStateWithLifecycle()
    val genderFilter by viewModel.brandListGenderFilterType.collectAsStateWithLifecycle()
    val brandItemList by viewModel.brandItemList.collectAsStateWithLifecycle()
    BrandListScreen(
        sortingMethod = sortingMethod,
        onSortingMethodChange = { viewModel.updateBrandListOrderFilterType(it) },
        genderFilter = genderFilter,
        onGenderFilterChange = { viewModel.updateBrandListGenderFilterType(it) },
        brandItemList = brandItemList,
        onItemClick = onItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BrandListScreen(
    sortingMethod: SortingMethod,
    onSortingMethodChange: (SortingMethod) -> Unit,
    genderFilter: ItemGender,
    onGenderFilterChange: (ItemGender) -> Unit,
    brandItemList: List<BrandMainResponseData>,
    onItemClick: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFilterBottomSheetShow by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = sortingMethod, key2 = genderFilter) {
        lazyListState.animateScrollToItem(0)
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isFilterBottomSheetShow) {
            ProductItemFilterBottomSheet(onDismissRequest = { isFilterBottomSheetShow = false }) {
                items(items = SortingMethod.values()) { sortingMethod ->
                    ProductItemFilterBottomSheetItem(
                        sheetState = sheetState,
                        value = sortingMethod.string,
                        onValueChange = {
                            onSortingMethodChange(sortingMethod)
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
                    textFiledOnClick = {  },
                    alertOnClick = {  }
                )
            }
        ) { paddingValue ->
            Column(
                modifier = Modifier.padding(paddingValue)
            ) {
                BrandFilterTab(
                    genderFilter = genderFilter,
                    onGenderFilterChange = { onGenderFilterChange(it) },
                    sortingMethod = sortingMethod,
                    onSortingMethodChange = { isFilterBottomSheetShow = true }
                )
                LazyColumn(
                    state = lazyListState
                ) {
                    items(
                        items = brandItemList,
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
                        Surface(
                            modifier = Modifier.padding(scaffoldPaddingValueCompositionLocal.current),
                            content = {  }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BrandScreenListPreview() {
    ABLEBODY_AndroidTheme {
        BrandListScreen(
            sortingMethod = SortingMethod.POPULAR,
            onSortingMethodChange = {},
            genderFilter = ItemGender.UNISEX,
            onGenderFilterChange = {},
            brandItemList = listOf(BrandMainResponseData(name="NIKE", id=3, thumbnail="", subName="나이키", brandGender=ItemGender.UNISEX, maxDiscount=0), BrandMainResponseData(name="Positive Me", id=36, thumbnail="", subName="포지티브미", brandGender= ItemGender.FEMALE, maxDiscount=0), BrandMainResponseData(name="MAVRK", id=30, thumbnail="", subName="매버릭", brandGender= ItemGender.MALE, maxDiscount=46), BrandMainResponseData(name="adidas", id=1, thumbnail="", subName="아디다스", brandGender=ItemGender.UNISEX, maxDiscount=0)),
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

        IconButton(
            onClick = {  },
            modifier = Modifier.constrainAs(ref = chevronButton) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                absoluteRight.linkTo(parent.absoluteRight)
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.chevronforward),
                contentDescription = "chevronForwardButton",
            )
        }
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
