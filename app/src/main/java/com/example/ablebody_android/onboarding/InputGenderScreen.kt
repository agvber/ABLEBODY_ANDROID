package com.example.ablebody_android.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.R
import com.example.ablebody_android.onboarding.utils.compose.BottomCustomButtonLayout
import com.example.ablebody_android.onboarding.utils.compose.HighlightText
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.PlaneGrey
import com.example.ablebody_android.ui.theme.SmallTextGrey
import com.example.ablebody_android.ui.theme.White


@Composable
fun SelectGenderButton(
    text: String,
    isChecked: Boolean,
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isChecked) AbleBlue else PlaneGrey
        ),
        shape = RoundedCornerShape(5.dp),
        onClick = { /*TODO: 성별 선택 이벤트*/ }
    ) {
        Text(text = text, color = if (isChecked) White else SmallTextGrey)
    }
}

@Preview(showBackground = true)
@Composable
fun SelectGenderButtonPreview() {
    SelectGenderButton("남자", false)
}

@Composable
fun InputGenderLayout() {
    Column {
        Text(
            text = "성별",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                fontWeight = FontWeight(400),
                color = SmallTextGrey,
            ),
            modifier = Modifier.padding(top=15.dp)
        )
        Row {
            SelectGenderButton(text = "남자", isChecked = true)
            SelectGenderButton(text = "여자", isChecked = false)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InputGenderLayoutPreview() {
    InputGenderLayout()
}


@Composable
fun InputGenderScreen() {
    BottomCustomButtonLayout(
        buttonText = "확인",
        onClick = {  }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            HighlightText(
                string = "성별을 입력해주세요",
                colorStringList = listOf("성별"),
                color = AbleBlue,
                style = TextStyle(
                    fontSize = 22.sp,
                    lineHeight = 35.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                    fontWeight = FontWeight(700),
                    color = AbleDark,
                )
            )
            InputGenderLayout()
            InputNicknameLayout()
            InputPhoneNumberLayout(value = "01012345678") {  }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun InputGenderScreenPreview() {
    InputGenderScreen()
}