package com.example.ablebody_android.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.R
import com.example.ablebody_android.utils.BottomCustomButtonLayout
import com.example.ablebody_android.utils.CustomTextField
import com.example.ablebody_android.utils.HighlightText
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.utils.CustomHintTextField


@Composable
fun InputPhoneNumberLayout(
    value: String,
    onValueChange: (String) -> Unit
) {
    CustomHintTextField(
        hintText = "휴대폰 번호",
        value = value,
        onValueChange = onValueChange
    )
}

@Preview(showBackground = true)
@Composable
fun InputPhoneNumberLayoutPreview() {
    var textState by remember { mutableStateOf("") }

    InputPhoneNumberLayout(
        value = textState,
        onValueChange = { textState = it }
    )
}


@Composable
private fun InputPhoneNumberContent(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        HighlightText(
            string = "애블바디는 휴대폰 번호로 가입해요.\n휴대폰 번호는 안전하게 보관되며\n어디에도 공개되지 않아요.",
            colorStringList = listOf("휴대폰 번호"),
            color = AbleBlue,
            style = TextStyle(
                fontSize = 22.sp,
                lineHeight = 35.sp,
                fontWeight = FontWeight(700),
                color = AbleDark,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black))
            )
        )
        InputPhoneNumberLayout(value, onValueChange)
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentPreview() {

    var state by remember{ mutableStateOf("") }

    InputPhoneNumberContent(state) { state = it }
}


@Composable
fun InputPhoneNumberScreen() {

    var state by remember{ mutableStateOf("") }

    BottomCustomButtonLayout(
        buttonText = "인증번호 받기",
        onClick = {  }
    ) {
        InputPhoneNumberContent(state) { state = it }
    }
}

@Preview(showBackground = true)
@Composable
fun InputPhoneNumberScreenPreview() {
    InputPhoneNumberScreen()
}