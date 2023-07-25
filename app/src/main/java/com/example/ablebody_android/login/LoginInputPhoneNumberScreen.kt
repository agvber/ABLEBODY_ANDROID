package com.example.ablebody_android.login

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
import com.example.ablebody_android.onboarding.InputPhoneNumberWithRuleLayout
import com.example.ablebody_android.utils.BottomCustomButtonLayout
import com.example.ablebody_android.utils.HighlightText
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark

@Composable
fun LoginInputPhoneNumberContent(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//        TopBarBackward()
        HighlightText(
            string = "휴대폰 번호(아이디)를 입력해주세요.",
            colorStringList = listOf("휴대폰 번호(아이디)"),
            color = AbleBlue,
            style = TextStyle(
                fontSize = 22.sp,
                lineHeight = 35.sp,
                fontWeight = FontWeight(700),
                color = AbleDark,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black))
            )
        )
        InputPhoneNumberWithRuleLayout(value, onValueChange)
        Column(
            Modifier
                .fillMaxWidth()
                /* TODO 여기 padding 설정을 어떻게 해야 하지? */
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "휴대폰 번호가 변경되었나요?")
            HighlightText(
                string = "카카오톡 채널로 계정 찾기",
                colorStringList = listOf("카카오톡 채널로 계정 찾기"),
                color = AbleBlue,
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    /* TODO: (온보딩/시작하기) 로그인 버튼 클릭 후 이벤트 */
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentPreview() {

    var state by remember{ mutableStateOf("") }

    LoginInputPhoneNumberContent(state) { state = it }
}


@Composable
fun LoginInputPhoneNumberScreen() {

    var state by remember{ mutableStateOf("") }
    TopBarBackward()
    BottomCustomButtonLayout(
        buttonText = "인증번호 받기",
        onClick = {  }
    ) {
        LoginInputPhoneNumberContent(state) { state = it }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginInputPhoneNumberScreenPreview() {
    LoginInputPhoneNumberScreen()
}