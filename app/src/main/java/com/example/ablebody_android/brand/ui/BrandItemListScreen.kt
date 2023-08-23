package com.example.ablebody_android.brand.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ablebody_android.Gender
import com.example.ablebody_android.brand.data.OrderFilterType
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.utils.DefaultFilterBottomSheet
import com.example.ablebody_android.ui.utils.DefaultFilterTab
import com.example.ablebody_android.ui.utils.DropDownFilterLayout
import com.example.ablebody_android.ui.utils.GenderSwitch
import com.example.ablebody_android.ui.utils.ProductItemListLayout
import com.example.ablebody_android.ui.utils.RoundedCornerCategoryFilterTab

@Composable
fun BrandItemListScreen() {
    var isFilterBottomSheetShow by remember { mutableStateOf(false) }
    var parentFilterState by remember { mutableStateOf("전체") }
    var orderFilterState by remember { mutableStateOf(OrderFilterType.Popularity) }
    var childFilterState by remember { mutableStateOf("") }
    var genderState by remember { mutableStateOf(Gender.MALE) }
    val context = LocalContext.current

    if (isFilterBottomSheetShow) {
        val filterBottomSheetValueList by remember {
            derivedStateOf {
                OrderFilterType.values().map { context.getString(it.stringResourceID) }
            }
        }
        DefaultFilterBottomSheet(
            valueList = filterBottomSheetValueList,
            onDismissRequest = { orderFilterType ->
                orderFilterType?.let { value ->
                    orderFilterState = OrderFilterType.values()
                        .filter { context.getString(it.stringResourceID) == value } [0]
                }
                isFilterBottomSheetShow = false
            }
        )
    }

    Column {
        DefaultFilterTab(
            filterItemList = listOf("전체", "상의", "하의", "아우터", "ACC"),
            value = parentFilterState,
            onValueChange = { parentFilterState = it },
            actionContent = {
                DropDownFilterLayout(
                    value = stringResource(id = orderFilterState.stringResourceID),
                    onClick = { isFilterBottomSheetShow = true }
                )
            }
        )
        RoundedCornerCategoryFilterTab(
            filterStringList = listOf("숏슬리브", "롱슬리브", "슬리브리스", "스웻&후디", "쇼츠", "팬츠", "레깅스"),
            value = childFilterState,
            onValueChange = { childFilterState = it }
        )
        Box {
            ProductItemListLayout()
            GenderSwitch(
                checked = genderState == Gender.MALE,
                onCheckedChange = { genderState = if (it) Gender.MALE else Gender.FEMALE },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = 10.dp, vertical = 25.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BrandItemListScreenPreview() {
    ABLEBODY_AndroidTheme {
        BrandItemListScreen()
    }
}