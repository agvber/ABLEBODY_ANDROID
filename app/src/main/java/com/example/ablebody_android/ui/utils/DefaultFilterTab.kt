package com.example.ablebody_android.ui.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.SmallTextGrey

@Composable
fun DefaultFilterTab(
    filterItemList: List<String>,
    value: String,
    onValueChange: (String) -> Unit,
    actionContent: @Composable () -> Unit = {  }
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyRow(
            modifier = Modifier.padding(start = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(items = filterItemList) { item ->
                val animateTextColor by animateColorAsState(
                    targetValue = if (item == value) AbleDark else SmallTextGrey,
                )
                val animateTextSize by animateIntAsState(
                    targetValue = if (item == value) 700 else 500
                )
                Text(
                    text = item,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight(animateTextSize),
                        color = animateTextColor,
                    ),
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onValueChange(item) }
                        )
                )
            }
        }
        actionContent()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultFilterTabPreview() {
    var state by remember { mutableStateOf("전체") }
    ABLEBODY_AndroidTheme {
        DefaultFilterTab(
            filterItemList = listOf("전체", "남자", "여자", "ACC"),
            value = state,
            onValueChange = { state = it }
        )
    }
}