package com.smilehunter.ablebody.ui.utils

import android.graphics.Color.parseColor
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.White

@Composable
fun GenderSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasure = rememberTextMeasurer()
    val animatePosition by animateDpAsState(targetValue = if (checked) 19.dp else 0.dp)
    val text = if (checked) "M" else "W"

    Box(
        modifier = modifier
            .padding(1.dp)
            .width(44.dp)
            .height(24.dp)
            .background(
                color = Color(parseColor("#B0B9C2")),
                shape = RoundedCornerShape(25.dp)
            )
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                enabled = true,
                role = Role.Switch,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
    ) {
        Canvas(
            modifier = Modifier
                .size(25.dp)
                .offset(x = animatePosition, y = 0.dp)
        ) {
            val radius = size.minDimension / 2.0f
            drawCircle(
                color = AbleBlue,
                radius = radius,
            )
            drawText(
                text = text,
                textMeasurer = textMeasure,
                topLeft = Offset(x = radius / 2f, 5f),
                style = TextStyle(
                    fontSize = 15.dp.toSp(),
                    fontWeight = FontWeight(500),
                    color = White,
                    textAlign = TextAlign.Center
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GenderSwitchPreview() {
    var state by remember { mutableStateOf(false) }
    GenderSwitch(checked = state, onCheckedChange = { state = it }, modifier = Modifier)
}