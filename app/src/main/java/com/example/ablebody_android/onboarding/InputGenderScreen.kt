package com.example.ablebody_android.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ablebody_android.Gender
import com.example.ablebody_android.R
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.PlaneGrey
import com.example.ablebody_android.ui.theme.SmallTextGrey
import com.example.ablebody_android.ui.theme.White
import com.example.ablebody_android.utils.BottomCustomButtonLayout
import com.example.ablebody_android.utils.HighlightText


@Composable
fun SelectGenderButton(
    text: String,
    isChecked: Boolean,
    onClick: () -> Unit
) {
    val buttonAnimatedColor by animateColorAsState(
        targetValue = if (isChecked) AbleBlue else PlaneGrey,
        animationSpec = tween(200)
    )
    val textAnimatedColor by animateColorAsState(
        targetValue = if (isChecked) White else SmallTextGrey,
        animationSpec = tween(100)
    )

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonAnimatedColor
        ),
        shape = RoundedCornerShape(5.dp),
        onClick = onClick
    ) {
        Text(text = text, color = textAnimatedColor)
    }
}

@Preview(showBackground = true)
@Composable
fun SelectGenderButtonPreview() {
    SelectGenderButton("남자", false) {}
}

@Composable
fun InputGenderLayout(
    gender: Gender?,
    onClick: (Gender) -> Unit
) {
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
            SelectGenderButton(
                text = Gender.MALE.name,
                isChecked = Gender.MALE == gender,
                onClick = { onClick(Gender.MALE) }
            )
            SelectGenderButton(
                text = Gender.FEMALE.name,
                isChecked = Gender.FEMALE == gender,
                onClick = { onClick(Gender.FEMALE) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InputGenderLayoutPreview() {
    var gender by remember { mutableStateOf<Gender>(Gender.MALE) }

    InputGenderLayout(gender = gender) {
        gender = it
    }
}


@Composable
fun InputGenderScreen(
    nickname: String,
    phoneNumber: String,
    gender: Gender?,
    onClick: (Gender) -> Unit
) {
    BottomCustomButtonLayout(
        buttonText = "확인",
        enable = gender != null,
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
            InputGenderLayout(gender = gender, onClick = onClick)
            InputNicknameLayout(nickname)  {  }
            InputPhoneNumberWithoutRuleLayout(value = phoneNumber, onValueChange = { })
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun InputGenderScreenPreview() {
    var gender by remember { mutableStateOf<Gender?>(null) }

    InputGenderScreen(
        nickname = "nick",
        phoneNumber = "01026289219",
        gender = gender,
        onClick = { gender = it }
    )
}