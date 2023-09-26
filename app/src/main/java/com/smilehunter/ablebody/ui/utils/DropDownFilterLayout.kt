package com.smilehunter.ablebody.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleDark

@Composable
fun DropDownFilterLayout(
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick,
        )
    ) {
        Text(
            text = value,
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight(400),
                color = AbleDark,
            ),
        )
        Image(
            painter = painterResource(id = R.drawable.chevrondown),
            contentDescription = "chevron down button",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DropDownFilterLayoutPreview() {
    ABLEBODY_AndroidTheme {
        DropDownFilterLayout(value = "인기순") {}
    }
}