package com.example.ablebody_android.brand.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.ItemChildCategory
import com.example.ablebody_android.ItemGender
import com.example.ablebody_android.ItemParentCategory
import com.example.ablebody_android.SortingMethod
import com.example.ablebody_android.brand.data.fakeBrandDetailItemResponseData
import com.example.ablebody_android.main.ui.scaffoldPaddingValueCompositionLocal
import com.example.ablebody_android.retrofit.dto.response.data.BrandDetailItemResponseData
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDeep
import com.example.ablebody_android.ui.utils.DefaultFilterTabItem
import com.example.ablebody_android.ui.utils.DefaultFilterTabRow
import com.example.ablebody_android.ui.utils.DropDownFilterLayout
import com.example.ablebody_android.ui.utils.GenderSwitch
import com.example.ablebody_android.ui.utils.InfiniteVerticalGrid
import com.example.ablebody_android.ui.utils.ProductItemFilterBottomSheet
import com.example.ablebody_android.ui.utils.ProductItemFilterBottomSheetItem
import com.example.ablebody_android.ui.utils.ProductItemLayout
import com.example.ablebody_android.ui.utils.RoundedCornerCategoryFilterTabItem
import com.example.ablebody_android.ui.utils.RoundedCornerCategoryFilterTabRow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BrandProductItemListLayout(
    sortingMethod: SortingMethod,
    onSortingMethodChange: (SortingMethod) -> Unit,
    parentFilter: ItemParentCategory,
    onParentFilterChange: (ItemParentCategory) -> Unit,
    itemChildCategory: List<ItemChildCategory>,
    childFilter: ItemChildCategory?,
    onChildFilterChange: (ItemChildCategory?) -> Unit,
    gender: ItemGender,
    onGenderChange: (ItemGender) -> Unit,
    productContentItem: List<BrandDetailItemResponseData.Item>,
    loadNextOnPageChangeListener: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var isFilterBottomSheetShow by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val roundedCornerCategoryFilterTabStateSheet = rememberLazyListState()
    val productItemGridState = rememberLazyGridState()

    if (isFilterBottomSheetShow) {
        ProductItemFilterBottomSheet(onDismissRequest = { isFilterBottomSheetShow = false }) {
            items(items = SortingMethod.values()) { sortingMethod ->
                ProductItemFilterBottomSheetItem(
                    sheetState = sheetState,
                    value = sortingMethod.string,
                    onValueChange = {
                        onSortingMethodChange(sortingMethod)
                        isFilterBottomSheetShow = false
                        scope.launch {
                            productItemGridState.animateScrollToItem(0)
                            roundedCornerCategoryFilterTabStateSheet.animateScrollToItem(0)
                        }
                    }
                )
            }
        }
    }

    Column {
        DefaultFilterTabRow(
            actionContent =  {
                DropDownFilterLayout(
                    value = sortingMethod.string,
                    onClick = { isFilterBottomSheetShow = true }
                )
            }
        ) {
            ItemParentCategory.values().forEach { category ->
                DefaultFilterTabItem(
                    selected = parentFilter == category,
                    text = category.string,
                    onClick = {
                        onParentFilterChange(category)
                        scope.launch {
                            productItemGridState.animateScrollToItem(0)
                            roundedCornerCategoryFilterTabStateSheet.animateScrollToItem(0)
                        }
                    }
                )
            }
        }

        RoundedCornerCategoryFilterTabRow(
            state = roundedCornerCategoryFilterTabStateSheet
        ) {
            items(itemChildCategory) { category ->
                RoundedCornerCategoryFilterTabItem(
                    selected = childFilter == category,
                    onClick = {
                        if (childFilter != category) {
                            onChildFilterChange(category)
                            scope.launch { productItemGridState.animateScrollToItem(0) }
                        } else {
                            onChildFilterChange(null)
                        }
                    }
                ) {
                    val textColor by animateColorAsState(
                        targetValue = if (childFilter == category) AbleBlue else AbleDeep
                    )
                    val textWeight by animateIntAsState(
                        targetValue = if (childFilter == category) 500 else 400
                    )
                    Text(
                        text = category.string,
                        style = TextStyle(
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight(textWeight),
                            color = textColor,
                            textAlign = TextAlign.Center,
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
        Box(
            Modifier.fillMaxSize()
        ) {
            InfiniteVerticalGrid(
                buffer = 4,
                lastPositionListener = loadNextOnPageChangeListener,
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                state = productItemGridState
            ) {
                items(
                    items = productContentItem,
                    key = { it.id }
                ) {
                    ProductItemLayout(
                        modifier = Modifier.animateItemPlacement(),
                        productName = it.name,
                        productPrice = it.price,
                        productSalePrice = it.salePrice,
                        brandName = it.brandName,
                        averageStarRating = it.avgStarRating,
                        thumbnail = it.image,
                        isSingleImage = it.isPlural
                    )
                }
                item {
                    Surface(
                        modifier = Modifier.padding(scaffoldPaddingValueCompositionLocal.current),
                        content = {  }
                    )
                }
            }
            GenderSwitch(
                checked = gender == ItemGender.MALE,
                onCheckedChange = { onGenderChange(if (it) ItemGender.MALE else ItemGender.FEMALE) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = 10.dp, vertical = 25.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BrandProductItemListLayoutPreview(
    orderFilterState: SortingMethod = SortingMethod.POPULAR,
    gender: ItemGender = ItemGender.UNISEX,
    parentFilterState: ItemParentCategory = ItemParentCategory.ALL,
    childFilterState: ItemChildCategory? = null,
    productContentItem: List<BrandDetailItemResponseData.Item> = fakeBrandDetailItemResponseData.content,
    itemChildCategory: List<ItemChildCategory> = ItemChildCategory.values().toList(),
    loadNextOnPageChangeListener: () -> Unit = {}
    ) {
    BrandProductItemListLayout(
        sortingMethod = orderFilterState,
        onSortingMethodChange = {  },
        gender = gender,
        onGenderChange = { },
        parentFilter = parentFilterState,
        onParentFilterChange = {  },
        childFilter = childFilterState,
        onChildFilterChange = { },
        productContentItem = productContentItem,
        itemChildCategory = itemChildCategory,
        loadNextOnPageChangeListener = loadNextOnPageChangeListener,
    )
}