package com.example.ablebody_android.brand.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ablebody_android.CodyItemFilterBottomSheetSportFilterType
import com.example.ablebody_android.CodyItemFilterBottomSheetTabFilterType
import com.example.ablebody_android.Gender
import com.example.ablebody_android.PersonHeightFilterType
import com.example.ablebody_android.R
import com.example.ablebody_android.retrofit.dto.response.data.BrandDetailCodyResponseData
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.utils.CodyItemFilterBottomSheet
import com.example.ablebody_android.ui.utils.CodyItemFilterTabRow
import com.example.ablebody_android.ui.utils.CodyItemFilterTabRowItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandCodyItemListLayout(
    codyItemListGenderFilterList: List<Gender>,
    onCodyItemListGenderFilterChange: (List<Gender>) -> Unit,
    codyItemListSportFilter: List<CodyItemFilterBottomSheetSportFilterType>,
    onCodyItemListSportFilterChange: (List<CodyItemFilterBottomSheetSportFilterType>) -> Unit,
    codyItemListPersonHeightFilter: PersonHeightFilterType,
    onCodyItemListPersonHeightFilterChange: (PersonHeightFilterType) -> Unit,
    codyItemList: BrandDetailCodyResponseData?
) {
    val scrollableState = rememberLazyGridState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isCodyItemFilterBottomSheetShow by remember { mutableStateOf(false) }
    var tabFilter by remember { mutableStateOf(CodyItemFilterBottomSheetTabFilterType.GENDER) }

    if (isCodyItemFilterBottomSheetShow) {
        CodyItemFilterBottomSheet(
            genderSelectList = codyItemListGenderFilterList,
            sportItemList = codyItemListSportFilter,
            personHeight = codyItemListPersonHeightFilter,
            onConfirmRequest = { genderFilterTypeList, sportFilterTypeList, personHeightFilterType ->
                onCodyItemListGenderFilterChange(genderFilterTypeList)
                onCodyItemListSportFilterChange(sportFilterTypeList)
                onCodyItemListPersonHeightFilterChange(personHeightFilterType)
            },
            onResetRequest = {  },
            onDismissRequest = { isCodyItemFilterBottomSheetShow = false },
            sheetState = sheetState,
            tabFilter = tabFilter,
            onTabFilterChange =  { tabFilter = it }
        )
    }

    Column {
        CodyItemFilterTabRow(
            resetRequest = { /*TODO*/ }
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
                }
            )
            CodyItemFilterTabRowItem(
                selected = !codyItemListSportFilter.contains(CodyItemFilterBottomSheetSportFilterType.ALL),
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
        if (codyItemList?.content != null) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = scrollableState,
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(items = codyItemList.content) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(data = it.imageURL)
                            .placeholder(R.drawable.cody_item_test)
                            .build(),
                        contentDescription = "cody recommended image",
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BrandCodyListScreenPreview(
    codyItemListGenderFilterList: List<Gender> = listOf(),
    onCodyItemListGenderFilterChange: (List<Gender>) -> Unit = {},
    codyItemListSportFilter: List<CodyItemFilterBottomSheetSportFilterType> = listOf(),
    onCodyItemListSportFilterChange: (List<CodyItemFilterBottomSheetSportFilterType>) -> Unit = {},
    codyItemListPersonHeightFilter: PersonHeightFilterType = PersonHeightFilterType.ALL,
    onCodyItemListPersonHeightFilterChange: (PersonHeightFilterType) -> Unit = {}
) {
    ABLEBODY_AndroidTheme {
        BrandCodyItemListLayout(
            codyItemListGenderFilterList = codyItemListGenderFilterList,
            onCodyItemListGenderFilterChange = onCodyItemListGenderFilterChange,
            codyItemListSportFilter = codyItemListSportFilter,
            onCodyItemListSportFilterChange = onCodyItemListSportFilterChange,
            codyItemListPersonHeightFilter = codyItemListPersonHeightFilter,
            onCodyItemListPersonHeightFilterChange = onCodyItemListPersonHeightFilterChange,
            codyItemList = null
        )
    }
}