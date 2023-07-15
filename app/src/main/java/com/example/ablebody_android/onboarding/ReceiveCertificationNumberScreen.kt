package com.example.ablebody_android.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.R
import com.example.ablebody_android.login.TopBarBackward
import com.example.ablebody_android.utils.BottomCustomButtonLayout
import com.example.ablebody_android.utils.CustomTextField
import com.example.ablebody_android.utils.HighlightText
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark

@Composable
fun ReceiveCertificationNumberContent(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)) {
//        TopBarBackward()
        HighlightText(
            string = "문자로 전송된\n인증번호 4자리를 입력해주세요.",
            colorStringList = listOf("인증번호 4자리"),
            color = AbleBlue,
            style = TextStyle(
                fontSize = 22.sp,
                lineHeight = 35.sp,
                fontWeight = FontWeight(700),
                color = AbleDark,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black))
            )
        )
//        InputPhoneNumberLayout(value, onValueChange)
        /*TODO hint로 "4자리 숫자". 어떤 값이 들어오면 인증번호 글자가 위에 떠야 함*/
        CustomTextField(
            labelText = "4자리 숫자",
            value = value,
            onValueChange = onValueChange
        )
        Text(
            text = " ", /*TODO 남은 초 보여주기*/
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                fontWeight = FontWeight(400),
                color = AbleBlue,
            )
        )
        Column(
            Modifier
                .fillMaxWidth()
                /* TODO 여기 padding 설정을 어떻게 해야 하지? */
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "인증문자가 안 오거나 문제가 있나요?")
            HighlightText(
                string = "카카오톡 채널로 문제 해결하기",
                colorStringList = listOf("카카오톡 채널로 문제 해결하기"),
                color = AbleBlue,
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {

                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentPreview() {

    var state by remember{ mutableStateOf("") }

    ReceiveCertificationNumberContent(state) { state = it }
}


@Composable
fun ReceiveCertificationNumberScreen() {

    var state by remember{ mutableStateOf("") }
    BottomCustomButtonLayout(
        buttonText = "인증번호 다시 받기",
        onClick = {  }
    ) {
        ReceiveCertificationNumberContent(state) { state = it }
    }
}

@Preview(showBackground = true)
@Composable
fun ReceiveCertificationNumberScreenPreview() {
    ReceiveCertificationNumberScreen()
}