package com.example.ablebody_android.ui.utils

import android.graphics.Color.parseColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.CodyItemFilterTabFilterType
import com.example.ablebody_android.R
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDeep
import com.example.ablebody_android.ui.theme.InactiveGrey
import com.example.ablebody_android.ui.theme.White

@Composable
fun CodyItemFilterTabLayout(
    modifier: Modifier = Modifier,
    selectedItemList: List<CodyItemFilterTabFilterType>,
    onItemSelectChange: (CodyItemFilterTabFilterType, Boolean) -> Unit
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_cody_tab_reset),
            contentDescription = "reset button"
        )
        LazyRow {
            items(
                items = CodyItemFilterTabFilterType.values()
            ) { filterType ->
                val isContains = selectedItemList.contains(filterType)
                val borderStrokeColor by animateColorAsState(
                    targetValue = if (isContains) AbleBlue else InactiveGrey
                )
                val backgroundColor by animateColorAsState(
                    targetValue = if (isContains) Color(parseColor("#E9F1FE")) else White
                )
                val textColor by animateColorAsState(
                    targetValue = if (isContains) AbleBlue else AbleDeep
                )
                val textWeight by animateIntAsState(
                    targetValue = if (isContains) 500 else 400
                )
                val interactionSource = remember { MutableInteractionSource() }
                Card(
                    shape = RoundedCornerShape(size = 50.dp),
                    backgroundColor = backgroundColor,
                    border = BorderStroke(width = 1.dp, color = borderStrokeColor),
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { onItemSelectChange(filterType, selectedItemList.contains(filterType)) }
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = stringResource(id = filterType.stringResourceID),
                            style = TextStyle(
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight(textWeight),
                                color = textColor,
                                textAlign = TextAlign.Center,
                            )
                        )
                        if (filterType.isPopup) {
                            Image(
                                painter = painterResource(id = R.drawable.chevrondown),
                                contentDescription = "chevrondown",
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CodyItemFilterTabLayoutPreview() {
    val state = remember { mutableStateListOf<CodyItemFilterTabFilterType>() }
    ABLEBODY_AndroidTheme {
        CodyItemFilterTabLayout(
            selectedItemList = state,
            onItemSelectChange = { codyFilterType, checked ->
                if (checked) state.remove(codyFilterType) else state.add(codyFilterType)
            }
        )
    }
}