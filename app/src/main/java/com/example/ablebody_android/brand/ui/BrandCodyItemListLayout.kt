package com.example.ablebody_android.brand.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ablebody_android.CodyItemFilterBottomSheetTabFilterType
import com.example.ablebody_android.Gender
import com.example.ablebody_android.HomeCategory
import com.example.ablebody_android.PersonHeightFilterType
import com.example.ablebody_android.R
import com.example.ablebody_android.brand.data.fakeBrandDetailCodyResponseData
import com.example.ablebody_android.main.ui.scaffoldPaddingValueCompositionLocal
import com.example.ablebody_android.retrofit.dto.response.data.BrandDetailCodyResponseData
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.utils.CodyItemFilterBottomSheet
import com.example.ablebody_android.ui.utils.CodyItemFilterTabRow
import com.example.ablebody_android.ui.utils.CodyItemFilterTabRowItem
import com.example.ablebody_android.ui.utils.InfiniteVerticalGrid
import com.example.ablebody_android.ui.utils.previewPlaceHolder

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BrandCodyItemListLayout(
    resetRequest: () -> Unit,
    codyItemListGenderFilterList: List<Gender>,
    onCodyItemListGenderFilterChange: (List<Gender>) -> Unit,
    codyItemListSportFilter: List<HomeCategory>,
    onCodyItemListSportFilterChange: (List<HomeCategory>) -> Unit,
    codyItemListPersonHeightFilter: PersonHeightFilterType,
    onCodyItemListPersonHeightFilterChange: (PersonHeightFilterType) -> Unit,
    codyItemContentList: List<BrandDetailCodyResponseData.Item>,
    loadNextOnPageChangeListener: () -> Unit
) {
    var isItemRefresh by rememberSaveable { mutableStateOf(false) }
    val scrollableState = rememberLazyGridState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isCodyItemFilterBottomSheetShow by remember { mutableStateOf(false) }
    var tabFilter by remember { mutableStateOf(CodyItemFilterBottomSheetTabFilterType.GENDER) }

    LaunchedEffect(key1 = isItemRefresh) {
        if (isItemRefresh) {
            scrollableState.animateScrollToItem(0)
            isItemRefresh = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isCodyItemFilterBottomSheetShow) {
            CodyItemFilterBottomSheet(
                genderSelectList = codyItemListGenderFilterList,
                sportItemList = codyItemListSportFilter,
                personHeight = codyItemListPersonHeightFilter,
                onConfirmRequest = { genderFilterTypeList, sportFilterTypeList, personHeightFilterType ->
                    onCodyItemListGenderFilterChange(genderFilterTypeList)
                    onCodyItemListSportFilterChange(sportFilterTypeList)
                    onCodyItemListPersonHeightFilterChange(personHeightFilterType)
                    isItemRefresh = true
                },
                onDismissRequest = { isCodyItemFilterBottomSheetShow = false },
                sheetState = sheetState,
                tabFilter = tabFilter,
                onTabFilterChange =  { tabFilter = it }
            )
        }
        Column {
            CodyItemFilterTabRow(
                resetRequest = { resetRequest(); isItemRefresh = true }
            ) {
                CodyItemFilterTabRowItem(
                    selected = codyItemListGenderFilterList.contains(Gender.MALE),
                    isPopup = false,
                    text = "남자",
                    onClick = {
                        codyItemListGenderFilterList.toMutableList().let {
                            if (it.contains(Gender.MALE)) { it.remove(Gender.MALE) } else { it.add(Gender.MALE) }
                            onCodyItemListGenderFilterChange(it)
                        }
                        isItemRefresh = true
                    }
                )
                CodyItemFilterTabRowItem(
                    selected = codyItemListGenderFilterList.contains(Gender.FEMALE),
                    isPopup = false,
                    text = "여자",
                    onClick = {
                        codyItemListGenderFilterList.toMutableList().let {
                            if (it.contains(Gender.FEMALE)) { it.remove(Gender.FEMALE) } else { it.add(Gender.FEMALE) }
                            onCodyItemListGenderFilterChange(it)
                        }
                        isItemRefresh = true
                    }
                )
                CodyItemFilterTabRowItem(
                    selected = codyItemListSportFilter.isNotEmpty(),
                    isPopup = true,
                    text = "종목",
                    onClick = {
                        tabFilter = CodyItemFilterBottomSheetTabFilterType.SPORT
                        isCodyItemFilterBottomSheetShow = true
                    }
                )
                CodyItemFilterTabRowItem(
                    selected = codyItemListPersonHeightFilter != PersonHeightFilterType.ALL,
                    isPopup = true,
                    text = "키",
                    onClick = {
                        tabFilter = CodyItemFilterBottomSheetTabFilterType.PERSON_HEIGHT
                        isCodyItemFilterBottomSheetShow = true
                    }
                )
            }
            InfiniteVerticalGrid(
                buffer = 6,
                lastPositionListener = loadNextOnPageChangeListener,
                columns = GridCells.Fixed(2),
                state = scrollableState,
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(
                    items = codyItemContentList,
                    key = { it.id }
                ) {
                    AsyncImage(
                        model = it.imageURL,
                        contentDescription = "cody recommended image",
                        contentScale = ContentScale.Crop,
                        placeholder = previewPlaceHolder(id = R.drawable.cody_item_test),
                        modifier = Modifier
                            .aspectRatio(.75f)
                            .animateItemPlacement(),
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    Box(modifier = Modifier.padding(scaffoldPaddingValueCompositionLocal.current))
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BrandCodyListScreenPreview(
    resetRequest: () -> Unit = {},
    codyItemListGenderFilterList: List<Gender> = listOf(),
    onCodyItemListGenderFilterChange: (List<Gender>) -> Unit = {},
    codyItemListSportFilter: List<HomeCategory> = listOf(),
    onCodyItemListSportFilterChange: (List<HomeCategory>) -> Unit = {},
    codyItemListPersonHeightFilter: PersonHeightFilterType = PersonHeightFilterType.ALL,
    onCodyItemListPersonHeightFilterChange: (PersonHeightFilterType) -> Unit = {},
    loadNextOnPageChangeListener: () -> Unit = {},
) {
    ABLEBODY_AndroidTheme {
        BrandCodyItemListLayout(
            resetRequest = resetRequest,
            codyItemListGenderFilterList = codyItemListGenderFilterList,
            onCodyItemListGenderFilterChange = onCodyItemListGenderFilterChange,
            codyItemListSportFilter = codyItemListSportFilter,
            onCodyItemListSportFilterChange = onCodyItemListSportFilterChange,
            codyItemListPersonHeightFilter = codyItemListPersonHeightFilter,
            onCodyItemListPersonHeightFilterChange = onCodyItemListPersonHeightFilterChange,
            codyItemContentList = fakeBrandDetailCodyResponseData.content,
            loadNextOnPageChangeListener = loadNextOnPageChangeListener
        )
    }
}