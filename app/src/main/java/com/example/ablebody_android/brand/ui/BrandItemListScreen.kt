package com.example.ablebody_android.brand.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.R
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.SmallTextGrey
import com.example.ablebody_android.ui.theme.White
import com.example.ablebody_android.ui.utils.DefaultFilterTab
import com.example.ablebody_android.ui.utils.DropDownFilterLayout
import com.example.ablebody_android.ui.utils.GenderSwitch
import com.example.ablebody_android.ui.utils.ProductItemListLayout
import com.example.ablebody_android.ui.utils.RoundedCornerCategoryFilterTab

@Composable
fun BrandDetailListScreen() {
    var parentFilterState by remember { mutableStateOf("전체") }
    var orderFilterState by remember { mutableStateOf("인기순") }
    var childFilterState by remember { mutableStateOf("") }
    var selectedTabIndexState by remember { mutableIntStateOf(1) }

    Column {
        TopAppBar(
            title = {
                Text(
                    text = "오옴",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(400),
                        color = AbleDark,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            },
            navigationIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_appbar_back_button),
                    contentDescription = "back button",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                // TODO: BackButton click
                            }
                        )
                )
            },
            backgroundColor = White,
        )
        TabRow(
            selectedTabIndex = selectedTabIndexState,
            contentColor = AbleBlue,
            indicator = @Composable { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndexState]),
                    color = AbleBlue
                )
            },
        ) {
            TabTextLayout(
                selected = selectedTabIndexState == 0,
                text = "아이템",
                onclick = { selectedTabIndexState = 0 }
            )
            TabTextLayout(
                selected = selectedTabIndexState == 1,
                text = "코디",
                onclick = { selectedTabIndexState = 1 }
            )
        }
        DefaultFilterTab(
            filterItemList = listOf("전체", "상의", "하의", "아우터", "ACC"),
            value = parentFilterState,
            onValueChange = { parentFilterState = it },
            actionContent = {
                DropDownFilterLayout(
                    value = orderFilterState,
                    onValueChange = { orderFilterState = it.name }
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
            var state by remember { mutableStateOf(false) }
            GenderSwitch(
                checked = state,
                onCheckedChange = { state = it },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = 10.dp, vertical = 25.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BrandDetailListScreenPreview() {
    ABLEBODY_AndroidTheme {
        BrandDetailListScreen()
    }
}

@Composable
private fun TabTextLayout(
    selected: Boolean,
    text: String,
    onclick: () -> Unit
) {
    val textColor by animateColorAsState(
        targetValue = if (selected) AbleBlue else SmallTextGrey
    )
    val textWeight by animateIntAsState(
        targetValue = if (selected) 500 else 400
    )
    val fontResourceID = if (selected) R.font.noto_sans_cjk_kr_regular else R.font.noto_sans_cjk_kr_medium
    Text(
        text = text,
        style = TextStyle(
            fontSize = 15.sp,
            fontFamily = FontFamily(Font(fontResourceID)),
            fontWeight = FontWeight(textWeight),
            color = textColor,
            textAlign = TextAlign.Center,
        ),
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onclick
        )
    )
}