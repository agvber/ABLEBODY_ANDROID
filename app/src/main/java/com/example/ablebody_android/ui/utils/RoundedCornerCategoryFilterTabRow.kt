package com.example.ablebody_android.ui.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDeep
import com.example.ablebody_android.ui.theme.InactiveGrey
import com.example.ablebody_android.ui.theme.White

@Composable
fun RoundedCornerCategoryFilterTabRow(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal =
        if (!reverseLayout) Arrangement.Start else Arrangement.End,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit
) {
    LazyRow(
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .nestedScroll(
                object : NestedScrollConnection {
                    override fun onPostScroll(
                        consumed: Offset,
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        return available.copy(y = 0f)
                    }
                }
            ),
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun RoundedCornerCategoryFilterTabRowPreview() {
    var state by remember { mutableStateOf("") }
    RoundedCornerCategoryFilterTabRow {
        items(listOf("숏슬리브", "롱슬리브", "슬리브리스", "스웻&후디", "쇼츠", "팬츠", "레깅스")) {
            RoundedCornerCategoryFilterTabItem(
                selected = state == it,
                onClick = { state = it }
            ) {
                val textColor by animateColorAsState(
                    targetValue = if (state == it) AbleBlue else AbleDeep
                )
                val textWeight by animateIntAsState(
                    targetValue = if (state == it) 500 else 400
                )
                Text(
                    text = it,
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
}

@Composable
fun RoundedCornerCategoryFilterTabItem(
    selected: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val borderStrokeColor by animateColorAsState(
        targetValue = if (selected) AbleBlue else InactiveGrey
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) Color(android.graphics.Color.parseColor("#E9F1FE")) else White
    )
    Card(
        shape = RoundedCornerShape(size = 50.dp),
        backgroundColor = backgroundColor,
        border = BorderStroke(width = 1.dp, color = borderStrokeColor),
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun RoundedCornerCategoryFilterTabItemPreview() {
    RoundedCornerCategoryFilterTabItem(
        selected = false,
        onClick = {  },
        ) {

    }
}