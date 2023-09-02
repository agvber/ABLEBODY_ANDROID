package com.example.ablebody_android.brand.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.ablebody_android.R
import com.example.ablebody_android.SortingMethod
import com.example.ablebody_android.retrofit.dto.response.data.BrandDetailItemResponseData
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDeep
import com.example.ablebody_android.ui.utils.DefaultFilterTabItem
import com.example.ablebody_android.ui.utils.DefaultFilterTabRow
import com.example.ablebody_android.ui.utils.DropDownFilterLayout
import com.example.ablebody_android.ui.utils.GenderSwitch
import com.example.ablebody_android.ui.utils.ProductItemFilterBottomSheet
import com.example.ablebody_android.ui.utils.ProductItemFilterBottomSheetItem
import com.example.ablebody_android.ui.utils.ProductItemLayout
import com.example.ablebody_android.ui.utils.RoundedCornerCategoryFilterTabItem
import com.example.ablebody_android.ui.utils.RoundedCornerCategoryFilterTabRow

@OptIn(ExperimentalMaterial3Api::class)
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
    productItems: BrandDetailItemResponseData?
) {
    var isFilterBottomSheetShow by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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

    Column {
        DefaultFilterTabRow(
            actionContent =  {
                DropDownFilterLayout(
                    value = sortingMethod.string,
                    onClick = { isFilterBottomSheetShow = true }
                )
            }
        ) {
            items(items = ItemParentCategory.values()) { category ->
                DefaultFilterTabItem(
                    selected = parentFilter == category,
                    text = category.string,
                    onClick = { onParentFilterChange(category) }
                )
            }
        }

        RoundedCornerCategoryFilterTabRow {
            items(itemChildCategory) { category ->
                RoundedCornerCategoryFilterTabItem(
                    selected = childFilter == category,
                    onClick = {
                        if (childFilter != category) {
                            onChildFilterChange(category)
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
            productItems?.let {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(items = productItems.content) {
                        ProductItemLayout(
                            productName = it.name,
                            productPrice = it.price,
                            productSalePrice = it.salePrice,
                            brandName = it.brandName,
                            averageStarRating = it.avgStarRating,
                            thumbnail = it.image,
                            isSingleImage = it.isPlural
                        )
                        // TODO: Infinite page 구상 할 것
                    }
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
    productItems: BrandDetailItemResponseData? = BrandDetailItemResponseData(content = listOf(BrandDetailItemResponseData.Item(id = 52, name = "나이키 스포츠웨어 에센셜", price = 35000, salePrice = null, brandName = "NIKE", image = R.drawable.product_item_test.toString(), isPlural = false, url = "", avgStarRating = null), BrandDetailItemResponseData.Item(id = 39, name = "나이키 드라이 핏 런 디비전 챌린저", price = 59000, salePrice = null, brandName = "NIKE", image = R.drawable.product_item_test.toString(), isPlural = false, url = "", avgStarRating = "5.0(1)")), pageable = BrandDetailItemResponseData.Pageable(sort = BrandDetailItemResponseData.Sort(empty = false, sorted = true, unsorted = false), offset = 0, pageNumber = 0, pageSize = 20, paged = true, unPaged = false), totalPages = 1, totalElements = 2, last = true, number = 0, sort = BrandDetailItemResponseData.Sort(empty = false, sorted = true, unsorted = false), size = 20, numberOfElements = 2, first = true, empty = false),
    itemChildCategory: List<ItemChildCategory> = ItemChildCategory.values().toList()
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
        productItems = productItems,
        itemChildCategory = itemChildCategory
    )
}