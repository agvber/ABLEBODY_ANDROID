package com.example.ablebody_android.ui.utils

import android.graphics.Color.parseColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDeep
import com.example.ablebody_android.ui.theme.InactiveGrey
import com.example.ablebody_android.ui.theme.White

@Composable
fun RoundedCornerCategoryFilterTab(
    filterStringList: List<String>,
    value: String,
    onValueChange: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        items(filterStringList) { text ->
            val borderStrokeColor by animateColorAsState(
                targetValue = if (value == text) AbleBlue else InactiveGrey
            )
            val backgroundColor by animateColorAsState(
                targetValue = if (value == text) Color(parseColor("#E9F1FE")) else White
            )
            val textColor by animateColorAsState(
                targetValue = if (value == text) AbleBlue else AbleDeep
            )
            val textWeight by animateIntAsState(
                targetValue = if (value == text) 500 else 400
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
                        onClick = { onValueChange(text) }
                    )
            ) {
                Text(
                    text = text,
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

@Preview(showBackground = true)
@Composable
fun RoundedCornerCategoryFilterTabPreview() {
    var state by remember { mutableStateOf("") }
    ABLEBODY_AndroidTheme {
        RoundedCornerCategoryFilterTab(
            filterStringList = listOf("숏슬리브", "롱슬리브", "슬리브리스", "스웻&후디", "쇼츠", "팬츠", "레깅스"),
            value = state,
            onValueChange = { state = it }
        )
    }
}