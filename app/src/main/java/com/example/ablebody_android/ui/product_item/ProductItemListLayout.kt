package com.example.ablebody_android.ui.product_item

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
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
import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.main.ui.scaffoldPaddingValueCompositionLocal
import com.example.ablebody_android.model.ProductItemData
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDeep
import com.example.ablebody_android.ui.utils.DefaultFilterTabItem
import com.example.ablebody_android.ui.utils.DefaultFilterTabRow
import com.example.ablebody_android.ui.utils.DropDownFilterLayout
import com.example.ablebody_android.ui.utils.GenderSwitch
import com.example.ablebody_android.ui.utils.InfiniteVerticalGrid
import com.example.ablebody_android.ui.utils.ProductItemFilterBottomSheet
import com.example.ablebody_android.ui.utils.ProductItemFilterBottomSheetItem
import com.example.ablebody_android.ui.utils.RoundedCornerCategoryFilterTabItem
import com.example.ablebody_android.ui.utils.RoundedCornerCategoryFilterTabRow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProductItemListLayout(
    modifier: Modifier = Modifier,
    itemClick: (Long) -> Unit,
    requestNextPage: () -> Unit,
    productContentItem: List<ProductItemData.Item>,
    onSortingMethodChange: (SortingMethod) -> Unit,
    onParentFilterChange: (ItemParentCategory) -> Unit,
    onChildFilterChange: (ItemChildCategory?) -> Unit,
    onGenderChange: (ItemGender) -> Unit,
    sortingMethod: SortingMethod,
    itemParentCategory: ItemParentCategory,
    itemChildCategory: ItemChildCategory?,
    gender: ItemGender
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val roundedCornerCategoryFilterTabStateSheet = rememberLazyListState()
    val productItemGridState = rememberLazyGridState()

    var isFilterBottomSheetShow by rememberSaveable { mutableStateOf(false) }

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

    Column(modifier = modifier) {
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
                    selected = itemParentCategory == category,
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

        val items by remember(itemParentCategory) {
            derivedStateOf {
                ItemChildCategory.values().filter {
                    when (itemParentCategory) {
                        ItemParentCategory.ALL -> true
                        else -> it.parentCategory == itemParentCategory
                    }
                }
            }
        }

        RoundedCornerCategoryFilterTabRow(
            state = roundedCornerCategoryFilterTabStateSheet
        ) {
            items(items) { category ->
                RoundedCornerCategoryFilterTabItem(
                    selected = itemChildCategory == category,
                    onClick = {
                        if (itemChildCategory != category) {
                            onChildFilterChange(category)
                            scope.launch { productItemGridState.animateScrollToItem(0) }
                        } else {
                            onChildFilterChange(null)
                        }
                    }
                ) {
                    val textColor by animateColorAsState(
                        targetValue = if (itemChildCategory == category) AbleBlue else AbleDeep
                    )
                    val textWeight by animateIntAsState(
                        targetValue = if (itemChildCategory == category) 500 else 400
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
                lastPositionListener = requestNextPage,
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                state = productItemGridState
            ) {
                items(
                    items = productContentItem,
                    key = { it.id }
                ) {
                    ProductItemLayout(
                        productName = it.name,
                        productPrice = it.price,
                        productSalePrice = it.salePrice,
                        brandName = it.brandName,
                        averageStarRating = it.avgStarRating,
                        thumbnail = it.imageURL,
                        isSingleImage = it.isSingleImage,
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { itemClick(it.id) }
                            )
                            .animateItemPlacement()
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    Box(modifier = Modifier.padding(scaffoldPaddingValueCompositionLocal.current))
                }
            }
            GenderSwitch(
                checked = gender == ItemGender.MALE,
                onCheckedChange = { onGenderChange(if (it) ItemGender.MALE else ItemGender.FEMALE) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = 10.dp, vertical = 25.dp)
                    .padding(scaffoldPaddingValueCompositionLocal.current)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductItemListLayoutPreview(
    orderFilterState: SortingMethod = SortingMethod.POPULAR,
    gender: ItemGender = ItemGender.UNISEX,
    parentFilterState: ItemParentCategory = ItemParentCategory.ALL,
    childFilterState: ItemChildCategory? = null,
    productContentItem: List<ProductItemData.Item> = emptyList(),
    itemChildCategory: ItemChildCategory? = ItemChildCategory.STRAP,
    loadNextOnPageChangeListener: () -> Unit = {}
) {
    ProductItemListLayout(
        itemClick = {},
        requestNextPage = loadNextOnPageChangeListener,
        productContentItem = productContentItem,
        onSortingMethodChange = {  },
        onParentFilterChange = {  },
        onChildFilterChange = { },
        onGenderChange = { },
        sortingMethod = orderFilterState,
        itemParentCategory = parentFilterState,
        itemChildCategory = itemChildCategory,
        gender = gender,
    )
}