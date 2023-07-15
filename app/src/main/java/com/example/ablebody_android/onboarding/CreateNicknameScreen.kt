package com.example.ablebody_android.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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


@Composable
fun InputNicknamewithSubtitleLayout(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        CustomTextField(
            labelText = "닉네임", value = value, onValueChange = onValueChange
        )
        // TODO: 텍스트 상황에 따라 바뀔 것
        Text(
            text = "20자 이내 영문, 숫자, 밑줄 및 마침표만 사용 가능해요.",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                fontWeight = FontWeight(400),
                color = AbleBlue,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InputNicknamewithSubtitleLayoutPreview() {
    var textState by remember { mutableStateOf("") }

    InputNicknamewithSubtitleLayout(
        value = textState,
        onValueChange = { textState = it }
    )
}

@Composable
fun InputNicknameLayout(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        CustomTextField(
            labelText = "닉네임", value = value, onValueChange = onValueChange
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InputNicknameLayoutPreview() {

    var state by remember{ mutableStateOf("") }
    InputNicknameLayout(state) { state = it }
}

@Composable
fun CreateNicknameScreen() {

    var state by remember{ mutableStateOf("") }

    BottomCustomButtonLayout(
        buttonText = "확인",
        onClick = {  }
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            HighlightText(
                string = "닉네임을 입력해주세요",
                colorStringList = listOf("닉네임"),
                color = AbleBlue,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 22.sp,
                    lineHeight = 35.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                    fontWeight = FontWeight(700),
                    color = AbleDark,
                )
            )
            InputNicknamewithSubtitleLayout(state) { state = it }
            InputPhoneNumberLayout(value = "01012345678") {  }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CreateNicknameScreenPreview() {
    CreateNicknameScreen()
}