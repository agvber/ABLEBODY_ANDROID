package com.smilehunter.ablebody.ui.cody_item

import android.graphics.Color.parseColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDeep
import com.smilehunter.ablebody.ui.theme.InactiveGrey
import com.smilehunter.ablebody.ui.theme.White

@Composable
fun CodyItemFilterTabRow(
    modifier: Modifier = Modifier,
    resetRequest: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_cody_tab_reset),
            contentDescription = "reset button",
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = resetRequest
            )
        )
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun CodyItemFilterTabRowPreview() {
    ABLEBODY_AndroidTheme {
        CodyItemFilterTabRow(resetRequest = {  }) {
            CodyItemFilterTabRowItem(
                text = "버튼",
                isPopup = true,
                selected = true,
                onClick = {}
            )
        }
    }
}

@Composable
fun CodyItemFilterTabRowItem(
    selected: Boolean,
    isPopup: Boolean,
    text: String,
    onClick: () -> Unit,
) {
    val borderStrokeColor by animateColorAsState(
        targetValue = if (selected) AbleBlue else InactiveGrey
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) Color(parseColor("#E9F1FE")) else White
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) AbleBlue else AbleDeep
    )
    val textWeight by animateIntAsState(
        targetValue = if (selected) 500 else 400
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
            )
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight(textWeight),
                    color = textColor,
                    textAlign = TextAlign.Center,
                )
            )
            if (isPopup) {
                Image(
                    painter = painterResource(id = R.drawable.chevrondown),
                    contentDescription = "chevrondown",
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CodyItemFilterTabRowItemPreview() {
    CodyItemFilterTabRowItem(
        text = "버튼",
        isPopup = true,
        selected = true,
        onClick = {}
    )
}