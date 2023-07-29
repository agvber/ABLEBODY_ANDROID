package com.example.ablebody_android.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.ablebody_android.ui.theme.SmallTextGrey

@Composable
fun CustomLabelText(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight(400),
            color = SmallTextGrey,
        )
    )
}