package com.example.ablebody_android.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.R
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleRed

@Composable
fun TextFieldUnderCorrectText(
    value: String
){
    Text(
        text = value,
        style = TextStyle(
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_black)),
            fontWeight = FontWeight(400),
            color = AbleBlue,
        )
    )
}

@Preview(showBackground = true)
@Composable
fun TextFieldUnderCorrectTextPreview() {
    TextFieldUnderCorrectText("사용 가능한 닉네임이에요.")
}

@Composable
fun TextFieldUnderWrongText(
    value: String
){
    Text(
        text = value,
        style = TextStyle(
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_black)),
            fontWeight = FontWeight(400),
            color = AbleRed,
        )
    )
}

@Preview(showBackground = true)
@Composable
fun TextFieldUnderWrongTextPreview() {
    TextFieldUnderWrongText("사용할 수 없는 닉네임이에요.")
}


@Composable
fun TextFieldUnderText(
    text: String,
    isPositive: Boolean = true
){
    val textColor = if (isPositive) AbleBlue else AbleRed

    Text(
        text = text,
        style = TextStyle(
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_black)),
            fontWeight = FontWeight(400),
            color = textColor,
        )
    )
}

@Preview(showBackground = true)
@Composable
fun TextFieldUnderTextPreview() {
    TextFieldUnderText("사용할 수 없는 닉네임이에요.", false)
}