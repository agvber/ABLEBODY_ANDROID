package com.smilehunter.ablebody.ui.cody_item

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.HomeCategory
import com.smilehunter.ablebody.data.dto.PersonHeightFilterType
import com.smilehunter.ablebody.model.CodyItemData
import com.smilehunter.ablebody.model.CodyItemFilterBottomSheetTabFilterType
import com.smilehunter.ablebody.model.fake.fakeCodyItemData
import com.smilehunter.ablebody.presentation.main.ui.scaffoldPaddingValueCompositionLocal
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.utils.previewPlaceHolder
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CodyItemListLayout(
    itemClick: (Long) -> Unit,
    resetRequest: () -> Unit,
    onCodyItemListGenderFilterChange: (List<Gender>) -> Unit,
    onCodyItemListSportFilterChange: (List<HomeCategory>) -> Unit,
    onCodyItemListPersonHeightFilterChange: (PersonHeightFilterType) -> Unit,
    modifier: Modifier = Modifier,
    codyItemListGenderFilterList: List<Gender>,
    codyItemListSportFilter: List<HomeCategory>,
    codyItemListPersonHeightFilter: PersonHeightFilterType,
    codyItemData: LazyPagingItems<CodyItemData.Item>
) {
    val scope = rememberCoroutineScope()
    val scrollableState = rememberLazyGridState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isCodyItemFilterBottomSheetShow by remember { mutableStateOf(false) }
    var tabFilter by remember { mutableStateOf(CodyItemFilterBottomSheetTabFilterType.GENDER) }

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
                    scope.launch { scrollableState.animateScrollToItem(0) }
                },
                onDismissRequest = { isCodyItemFilterBottomSheetShow = false },
                sheetState = sheetState,
                tabFilter = tabFilter,
                onTabFilterChange =  { tabFilter = it }
            )
        }
        Column(modifier = modifier) {
            CodyItemFilterTabRow(
                resetRequest = {
                    resetRequest()
                    scope.launch { scrollableState.animateScrollToItem(0) }
                }
            ) {
                CodyItemFilterTabRowItem(
                    selected = codyItemListGenderFilterList.contains(Gender.MALE),
                    isPopup = false,
                    text = "남자",
                    onClick = {
                        codyItemListGenderFilterList.toMutableList().let {
                            if (it.contains(Gender.MALE)) { it.remove(
                                Gender.MALE) } else { it.add(
                                Gender.MALE) }
                            onCodyItemListGenderFilterChange(it)
                        }
                        scope.launch { scrollableState.animateScrollToItem(0) }
                    }
                )
                CodyItemFilterTabRowItem(
                    selected = codyItemListGenderFilterList.contains(Gender.FEMALE),
                    isPopup = false,
                    text = "여자",
                    onClick = {
                        codyItemListGenderFilterList.toMutableList().let {
                            if (it.contains(Gender.FEMALE)) { it.remove(
                                Gender.FEMALE) } else { it.add(
                                Gender.FEMALE) }
                            onCodyItemListGenderFilterChange(it)
                        }
                        scope.launch { scrollableState.animateScrollToItem(0) }
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = scrollableState,
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(count = codyItemData.itemCount) { index ->
                    AsyncImage(
                        model = codyItemData[index]?.imageURL,
                        contentDescription = "cody recommended image",
                        contentScale = ContentScale.Crop,
                        placeholder = previewPlaceHolder(id = R.drawable.cody_item_test),
                        modifier = Modifier
                            .aspectRatio(.75f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { codyItemData[index]?.id?.let { itemClick(it) } }
                            )
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
fun CodyListScreenPreview(
    itemClick: (Long) -> Unit = {},
    resetRequest: () -> Unit = {},
    onCodyItemListGenderFilterChange: (List<Gender>) -> Unit = {},
    onCodyItemListSportFilterChange: (List<HomeCategory>) -> Unit = {},
    onCodyItemListPersonHeightFilterChange: (PersonHeightFilterType) -> Unit = {},
    codyItemListGenderFilterList: List<Gender> = emptyList(),
    codyItemListSportFilter: List<HomeCategory> = emptyList(),
    codyItemListPersonHeightFilter: PersonHeightFilterType = PersonHeightFilterType.ALL,
    codyItemData: LazyPagingItems<CodyItemData.Item> = flowOf(PagingData.from(fakeCodyItemData.content)).collectAsLazyPagingItems()
) {
    ABLEBODY_AndroidTheme {
        CodyItemListLayout(
            itemClick = {},
            resetRequest = resetRequest,
            codyItemListGenderFilterList = codyItemListGenderFilterList,
            onCodyItemListGenderFilterChange = onCodyItemListGenderFilterChange,
            codyItemListSportFilter = codyItemListSportFilter,
            onCodyItemListSportFilterChange = onCodyItemListSportFilterChange,
            codyItemListPersonHeightFilter = codyItemListPersonHeightFilter,
            onCodyItemListPersonHeightFilterChange = onCodyItemListPersonHeightFilterChange,
            codyItemData = codyItemData
        )
    }
}