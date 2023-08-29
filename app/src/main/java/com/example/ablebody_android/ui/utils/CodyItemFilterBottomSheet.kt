package com.example.ablebody_android.ui.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.CodyItemFilterBottomSheetPersonHeightFilterType
import com.example.ablebody_android.CodyItemFilterBottomSheetSportFilterType
import com.example.ablebody_android.CodyItemFilterBottomSheetTabFilterType
import com.example.ablebody_android.Gender
import com.example.ablebody_android.R
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDeep
import com.example.ablebody_android.ui.theme.InactiveGrey
import com.example.ablebody_android.ui.theme.SmallTextGrey
import com.example.ablebody_android.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodyItemFilterBottomSheet(
    tabFilter: CodyItemFilterBottomSheetTabFilterType,
    onTabFilterChange: (CodyItemFilterBottomSheetTabFilterType) -> Unit,
    gender: Gender,
    sportItemList: SnapshotStateList<CodyItemFilterBottomSheetSportFilterType>,
    personHeight: CodyItemFilterBottomSheetPersonHeightFilterType,
    onConfirmRequest: (Gender, SnapshotStateList<CodyItemFilterBottomSheetSportFilterType>, CodyItemFilterBottomSheetPersonHeightFilterType) -> Unit,
    onResetRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState,
) {
    var genderState by remember { mutableStateOf(gender) }
    val sportItemListState = remember { sportItemList }
    var personHeightState by remember { mutableStateOf(personHeight) }
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.height(500.dp),
        containerColor = White,
        sheetState = sheetState
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Column {
                CodyItemFilterBottomSheetFilterTabContent(
                    value = tabFilter,
                    onValueChange = { onTabFilterChange(it) }
                )
                when (tabFilter) {
                    CodyItemFilterBottomSheetTabFilterType.GENDER -> {
                        CodyItemFilterBottomSheetGenderContent(
                            value = genderState,
                            onChangeValue = { genderState = it }
                        )
                    }
                    CodyItemFilterBottomSheetTabFilterType.SPORT -> {
                        val sportList by remember { derivedStateOf { CodyItemFilterBottomSheetSportFilterType.values().filter { it != CodyItemFilterBottomSheetSportFilterType.ALL } } }
                        CodyItemFilterBottomSheetSportContent(
                            checkedItemList = sportItemListState,
                            onCheckedChange = { sport, checked ->
                                when (sport) {
                                    CodyItemFilterBottomSheetSportFilterType.ALL -> {
                                        if (checked) sportItemListState.clear() else sportItemListState.addAll(CodyItemFilterBottomSheetSportFilterType.values())
                                    }
                                    else -> {
                                        if (checked) sportItemListState.remove(sport) else sportItemListState.add(sport)
                                        if (sportItemListState.containsAll(sportList)) {
                                            sportItemListState.add(CodyItemFilterBottomSheetSportFilterType.ALL)
                                        } else {
                                            sportItemListState.remove(CodyItemFilterBottomSheetSportFilterType.ALL)
                                        }
                                    }
                                }
                            }
                        )
                    }
                    CodyItemFilterBottomSheetTabFilterType.PERSON_HEIGHT -> {
                        CodyItemFilterBottomSheetPersonHeightContent(
                            value = personHeightState,
                            onValueChange = { personHeightState = it }
                        )
                    }
                }
            }
            CodyFilterBottomSheetBottom(
                modifier = Modifier.align(Alignment.BottomCenter),
                onResetRequest = onResetRequest,
                onConfirmRequest = {
                    onConfirmRequest(genderState, sportItemListState, personHeightState)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CodyItemFilterBottomSheetPreview() {
    ABLEBODY_AndroidTheme {
        val sheetState = rememberModalBottomSheetState()
        var tabFilter by remember { mutableStateOf(CodyItemFilterBottomSheetTabFilterType.GENDER) }
        var gender by remember { mutableStateOf(Gender.MALE) }
        var sportItemList = remember { mutableStateListOf<CodyItemFilterBottomSheetSportFilterType>() }
        var personHeight by remember { mutableStateOf(CodyItemFilterBottomSheetPersonHeightFilterType.ALL) }
        CodyItemFilterBottomSheet(
            gender = gender,
            sportItemList = sportItemList,
            personHeight = personHeight,
            onConfirmRequest = { genderType, sportFilterTypeList, personHeightFilterType ->
                gender = genderType
                sportItemList = sportFilterTypeList
                personHeight = personHeightFilterType
            },
            onResetRequest = {  },
            onDismissRequest = {  },
            sheetState = sheetState,
            tabFilter = tabFilter,
            onTabFilterChange =  { tabFilter = it }
        )
    }
}

@Composable
private fun CodyItemFilterBottomSheetFilterTabContent(
    modifier: Modifier = Modifier,
    value: CodyItemFilterBottomSheetTabFilterType,
    onValueChange: (CodyItemFilterBottomSheetTabFilterType) -> Unit
) {
    LazyRow(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(CodyItemFilterBottomSheetTabFilterType.values()) { filter ->
            val textColor by animateColorAsState(
                targetValue = if (filter == value) Color(0xFF0C0C0D) else SmallTextGrey
            )
            val interactionSource = remember { MutableInteractionSource() }
            Text(
                text = filter.string,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(700),
                    color = textColor,
                ),
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { onValueChange(filter) }
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CodyItemFilterBottomSheetFilterTabContentPreview() {
    ABLEBODY_AndroidTheme {
        var state by remember { mutableStateOf(CodyItemFilterBottomSheetTabFilterType.GENDER) }
        CodyItemFilterBottomSheetFilterTabContent(
            value = state,
            onValueChange = { state = it }
        )
    }
}

@Composable
private fun CodyFilterBottomSheetBottom(
    modifier: Modifier = Modifier,
    onResetRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onResetRequest,
            shape = RoundedCornerShape(size = 4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = White,
            ),
            border = BorderStroke(width = 1.dp, color = InactiveGrey),
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_cody_tab_reset),
                contentDescription = "reset button",
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 2.dp)
            )
            Text(
                text = "초기화",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 21.sp,
                    fontWeight = FontWeight(500),
                    color = AbleDeep,
                    textAlign = TextAlign.Justify,
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
        Button(
            onClick = onConfirmRequest,
            shape = RoundedCornerShape(size = 4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AbleBlue,
            ),
            modifier = Modifier
                .weight(1.8f)
        ) {
            Text(
                text = "필터 적용",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 21.sp,
                    fontWeight = FontWeight(500),
                    color = White,
                    textAlign = TextAlign.Justify,
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CodyFilterBottomSheetBottomPreview() {
    CodyFilterBottomSheetBottom(
        onResetRequest = {  },
        onConfirmRequest = {  }
    )
}

@Composable
private fun CodyItemFilterBottomSheetGenderContent(
    modifier: Modifier = Modifier,
    value: Gender,
    onChangeValue: (Gender) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        for (gender in Gender.values()) {
            val textColor by animateColorAsState(
                targetValue = if (gender == value) AbleBlue else Color(0xFF505863)
            )
            val backgroundColor by animateColorAsState(
                targetValue = if (gender == value) Color(0xFFE9F1FE) else White
            )
            val strokeColor by animateColorAsState(
                targetValue = if (gender == value) AbleBlue else Color(0xFFCCE1FF)
            )
            val interactionSource = remember { MutableInteractionSource() }
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onChangeValue(gender) }
                    ),
                color = backgroundColor,
                border = BorderStroke(width = 1.dp, color = strokeColor),
                shape = RoundedCornerShape(size = 4.dp),
            ) {
                Text(
                    text = stringResource(id = gender.resourceID),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(400),
                        color = textColor,
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CodyItemFilterBottomSheetGenderContentPreview() {
    ABLEBODY_AndroidTheme {
        var state by remember { mutableStateOf(Gender.MALE) }
        CodyItemFilterBottomSheetGenderContent(
            value = state,
            onChangeValue = { state = it }
        )
    }
}

@Composable
private fun CodyItemFilterBottomSheetSportContent(
    modifier: Modifier = Modifier,
    checkedItemList: List<CodyItemFilterBottomSheetSportFilterType>,
    onCheckedChange: (CodyItemFilterBottomSheetSportFilterType, Boolean) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = CodyItemFilterBottomSheetSportFilterType.values()) { sport ->
            val interaction = remember { MutableInteractionSource() }
            Row(
                modifier = Modifier.clickable(
                    interactionSource = interaction,
                    indication = null,
                    onClick = { onCheckedChange(sport, checkedItemList.contains(sport)) }
                )
            ) {
                val checkBoxResourceID = if (checkedItemList.contains(sport)) {
                    R.drawable.ic_product_item_filter_bottom_sheet_check_box_enable
                } else {
                    R.drawable.ic_product_item_filter_bottom_sheet_check_box_disable
                }
                Image(
                    painter = painterResource(id = checkBoxResourceID),
                    contentDescription = "check box"
                )
                Text(
                    text = sport.string,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000),
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CodyItemFilterBottomSheetSportContentPreview() {
    val state = remember { mutableStateListOf<CodyItemFilterBottomSheetSportFilterType>() }
    val sportList = CodyItemFilterBottomSheetSportFilterType.values().filter { it != CodyItemFilterBottomSheetSportFilterType.ALL }
    CodyItemFilterBottomSheetSportContent(
        checkedItemList = state,
        onCheckedChange = { sport, checked ->
            when (sport) {
                CodyItemFilterBottomSheetSportFilterType.ALL -> {
                    if (checked) state.clear() else state.addAll(CodyItemFilterBottomSheetSportFilterType.values())
                }
                else -> {
                    if (checked) state.remove(sport) else state.add(sport)

                    if (state.containsAll(sportList)) {
                        state.add(CodyItemFilterBottomSheetSportFilterType.ALL)
                    } else {
                        state.remove(CodyItemFilterBottomSheetSportFilterType.ALL)
                    }
                }
            }
        }
    )
}
@Composable
private fun CodyItemFilterBottomSheetPersonHeightContent(
    modifier: Modifier = Modifier,
    value: CodyItemFilterBottomSheetPersonHeightFilterType,
    onValueChange: (CodyItemFilterBottomSheetPersonHeightFilterType) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = CodyItemFilterBottomSheetPersonHeightFilterType.values()) { personHeight ->
            val interaction = remember { MutableInteractionSource() }
            Row(
                modifier = Modifier.clickable(
                    interactionSource = interaction,
                    indication = null,
                    onClick = { onValueChange(personHeight) }
                )
            ) {
                val checkBoxResourceID = if (value == personHeight) {
                    R.drawable.ic_product_item_filter_bottom_sheet_toggle_box_enable
                } else {
                    R.drawable.ic_product_item_filter_bottom_sheet_toggle_box_disable
                }
                Image(
                    painter = painterResource(id = checkBoxResourceID),
                    contentDescription = "check box"
                )
                Text(
                    text = personHeight.string,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000),
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CodyItemFilterBottomSheetPersonHeightContentPreview() {
    var state by remember { mutableStateOf(CodyItemFilterBottomSheetPersonHeightFilterType.ALL) }
    CodyItemFilterBottomSheetPersonHeightContent(
        value = state,
        onValueChange = { height ->
            state = height
        }
    )
}