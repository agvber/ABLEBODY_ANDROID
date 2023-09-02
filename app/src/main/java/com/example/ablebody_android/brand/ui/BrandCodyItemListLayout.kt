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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ablebody_android.PersonHeightFilterType
import com.example.ablebody_android.CodyItemFilterBottomSheetSportFilterType
import com.example.ablebody_android.CodyItemFilterBottomSheetTabFilterType
import com.example.ablebody_android.CodyItemFilterTabFilterType
import com.example.ablebody_android.Gender
import com.example.ablebody_android.R
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.utils.CodyItemFilterBottomSheet
import com.example.ablebody_android.ui.utils.CodyItemFilterTabRow
import com.example.ablebody_android.ui.utils.CodyItemFilterTabRowItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandCodyItemListLayout() {
    val codyFilterSelectList = remember { mutableStateListOf<CodyItemFilterTabFilterType>() }
    val scrollableState = rememberLazyGridState()
    var isCodyItemFilterBottomSheetShow by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var tabFilter by remember { mutableStateOf(CodyItemFilterBottomSheetTabFilterType.GENDER) }
    val genderSelectList: SnapshotStateList<Gender> = remember { mutableStateListOf() }
    val sportItemList = remember { mutableStateListOf<CodyItemFilterBottomSheetSportFilterType>() }
    var personHeight by remember { mutableStateOf(PersonHeightFilterType.ALL) }

    if (isCodyItemFilterBottomSheetShow) {
        CodyItemFilterBottomSheet(
            genderSelectList = genderSelectList,
            sportItemList = sportItemList,
            personHeight = personHeight,
            onConfirmRequest = { genderFilterTypeList, sportFilterTypeList, personHeightFilterType ->
                genderSelectList.apply { clear(); addAll(genderFilterTypeList) }
                sportItemList.apply { clear(); addAll(sportFilterTypeList) }
                personHeight = personHeightFilterType

                if (genderFilterTypeList.contains(Gender.MALE)) {
                    codyFilterSelectList.add(CodyItemFilterTabFilterType.MALE)
                } else {
                    codyFilterSelectList.remove(CodyItemFilterTabFilterType.MALE)
                }

                if (genderFilterTypeList.contains(Gender.FEMALE)) {
                    codyFilterSelectList.add(CodyItemFilterTabFilterType.FEMALE)
                } else {
                    codyFilterSelectList.remove(CodyItemFilterTabFilterType.FEMALE)
                }

                if (sportItemList.containsAll(CodyItemFilterBottomSheetSportFilterType.values().toList())) {
                    codyFilterSelectList.remove(CodyItemFilterTabFilterType.SPORT)
                } else {
                    codyFilterSelectList.add(CodyItemFilterTabFilterType.SPORT)
                }

                if (personHeight == PersonHeightFilterType.ALL) {
                    codyFilterSelectList.remove(CodyItemFilterTabFilterType.HEIGHT)
                } else {
                    codyFilterSelectList.add(CodyItemFilterTabFilterType.HEIGHT)
                }
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
            for (value in CodyItemFilterTabFilterType.values()) {
                val isSelected = codyFilterSelectList.contains(value)
                CodyItemFilterTabRowItem(
                    selected = isSelected,
                    isPopup = value.isPopup,
                    text = stringResource(id = value.stringResourceID),
                    onClick = {
                        if (value == CodyItemFilterTabFilterType.SPORT || value == CodyItemFilterTabFilterType.HEIGHT) {
                            tabFilter = CodyItemFilterBottomSheetTabFilterType.PERSON_HEIGHT
                            isCodyItemFilterBottomSheetShow = true
                        } else {
                            if (isSelected) {
                                codyFilterSelectList.remove(value)
                            } else {
                                codyFilterSelectList.add(value)
                            }
                        }
                    }
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = scrollableState,
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(items = (0..30).toList()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data = R.drawable.cody_item_test)
                        .placeholder(R.drawable.cody_item_test)
                        .build(),
                    contentDescription = "cody recommended image",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BrandCodyListScreenPreview() {
    ABLEBODY_AndroidTheme {
        BrandCodyItemListLayout()
    }
}