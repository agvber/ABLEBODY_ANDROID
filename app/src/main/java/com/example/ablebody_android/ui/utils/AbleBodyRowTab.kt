package com.example.ablebody_android.ui.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.R
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.SmallTextGrey

@Composable
fun AbleBodyRowTab(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = TabRowDefaults.containerColor,
    contentColor: Color = AbleBlue ,
    indicator: @Composable (List<TabPosition>) -> Unit = @Composable { tabPositions ->
        TabRowDefaults.Indicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
            color = AbleBlue
        )
    },
    divider: @Composable () -> Unit = @Composable {
        Divider()
    },
    tabs: @Composable () -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        indicator = indicator,
        divider = divider,
        tabs = tabs
    )
}

@Composable
fun AbleBodyTabItem(
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