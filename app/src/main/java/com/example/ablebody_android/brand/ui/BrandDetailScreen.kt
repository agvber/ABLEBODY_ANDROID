package com.example.ablebody_android.brand.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrandDetailScreen() {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })

    Scaffold(
        topBar = {
            BrandDetailTopBarLayout(
                titleText = "오옴",
                backButtonClicked = { /*TODO*/ },
                selectedTabIndex = pagerState.currentPage,
                tabOnClick = { scope.launch { pagerState.animateScrollToPage(it) } }
            )
        },
        content = { paddingValue ->
            Surface(
                modifier = Modifier.padding(paddingValue)
            ) {
                HorizontalPager(
                    state = pagerState,
                ) {
                    when(it) {
                        0 -> BrandItemListScreen()
                        1 -> BrandCodyListScreen()
                    }
                }
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun BrandDetailScreenPreview() {
    ABLEBODY_AndroidTheme {
        BrandDetailScreen()
    }
}

@Composable
private fun BrandDetailTopBarLayout(
    titleText: String,
    backButtonClicked: () -> Unit,
    selectedTabIndex: Int,
    tabOnClick: (Int) -> Unit
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = titleText,
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
                            onClick = backButtonClicked
                        )
                )
            },
            backgroundColor = White,
        )
        TabRow(
            selectedTabIndex = selectedTabIndex,
            contentColor = AbleBlue,
            indicator = @Composable { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = AbleBlue
                )
            },
        ) {
            TabTextLayout(
                selected = selectedTabIndex == 0,
                text = "아이템",
                onclick = { tabOnClick(0) }
            )
            TabTextLayout(
                selected = selectedTabIndex == 1,
                text = "코디",
                onclick = { tabOnClick(1) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BrandDetailTopBarLayoutPreview() {
    var state by remember { mutableIntStateOf(0) }
    ABLEBODY_AndroidTheme {
        BrandDetailTopBarLayout(
            titleText = "오옴",
            backButtonClicked = { /*TODO*/ },
            selectedTabIndex = state,
            tabOnClick = { state = it }
        )
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