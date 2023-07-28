package com.example.ablebody_android.onboarding


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ablebody_android.R
import com.example.ablebody_android.onboarding.utils.convertMillisecondsToFormattedTime
import com.example.ablebody_android.utils.BottomCustomButtonLayout
import com.example.ablebody_android.utils.CustomTextField
import com.example.ablebody_android.utils.HighlightText
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.utils.TextFieldUnderText
import com.example.ablebody_android.utils.redirectToURL


@Composable
fun InputCertificationNumberTitle() {
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
}

@Preview(showBackground = true)
@Composable
fun InputCertificationNumberTitlePreview() {
    InputCertificationNumberTitle()
}

@Composable
fun InputCertificationNumberTextField(
    underTextValue: String,
    underTextIsPositive: Boolean,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        /*TODO hint로 "4자리 숫자". 어떤 값이 들어오면 인증번호 글자가 위에 떠야 함*/
        CustomTextField(
            labelText = "4자리 숫자",
            value = value,
            onValueChange = onValueChange
        )
        TextFieldUnderText(
            text = underTextValue, /*TODO 남은 초 보여주기*/
            isPositive = underTextIsPositive
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InputCertificationNumberTextFieldPreview() {
    var state by remember { mutableStateOf("") }
    InputCertificationNumberTextField(
        underTextValue = "2분 30초",
        value = state,
        underTextIsPositive = true
    ) { state = it }
}

@Composable
fun RedirectToCustomerSupport() {
    Column(
        Modifier
            .fillMaxWidth()
            /* TODO 여기 padding 설정을 어떻게 해야 하지? */
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val context = LocalContext.current
        Text(text = "인증문자가 안 오거나 문제가 있나요?")
        HighlightText(
            string = "카카오톡 채널로 문제 해결하기",
            colorStringList = listOf("카카오톡 채널로 문제 해결하기"),
            color = AbleBlue,
            modifier = Modifier.clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
            ) {
                redirectToURL(context, "kakaotalk channel")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RedirectToCustomerSupportPreview() {
    RedirectToCustomerSupport()
}

@Composable
fun InputCertificationNumberContent(
    underTextValue: String,
    underTextIsPositive: Boolean,
    textFieldValue: String,
    textFieldOnValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        InputCertificationNumberTitle()
        InputCertificationNumberTextField(
            underTextValue = underTextValue,
            underTextIsPositive = underTextIsPositive,
            value = textFieldValue,
            onValueChange = textFieldOnValueChange
        )
        RedirectToCustomerSupport()
    }
}

@Preview(showBackground = true)
@Composable
private fun InputCertificationNumberContentPreview() {
    var state by remember{ mutableStateOf("") }

    InputCertificationNumberContent(
        "2분 30초",
        true,
        state
    ) { state = it }
}

// TODO: 인증번호 다시 받기 요청 할때 전화 번호가 필요함 !!! 저는 10번 넘겨서 테스트를 못합니다 나희님 파이팅 ㅋㅋㅋㅋ
@Composable
fun InputCertificationNumberScreen(
    viewModel: OnboardingViewModel,
    navController: NavController
) {
    val currentTimeState by viewModel.currentCertificationNumberTimeLiveData.observeAsState(180000L)
    val sendSMSLiveDataState by viewModel.sendSMSLiveData.observeAsState()

    var textFieldStringState by rememberSaveable { mutableStateOf("") }

    val checkSMSLiveDataState by viewModel.checkSMSLiveData.observeAsState()

    val underTextValue: String by remember(currentTimeState) {
        derivedStateOf {
            if (currentTimeState != 0L) {
                if ("\\d{4}".toRegex().matches(textFieldStringState)) {
                    sendSMSLiveDataState?.data?.phoneConfirmId?.let { viewModel.checkSMS(it, textFieldStringState) }
                    if (checkSMSLiveDataState?.success == true) {
                        navController.navigate("CreateNickname")
                        ""
                    } else {
                        "인증번호가 올바르지 않아요!"
                    }
                } else {
                    convertMillisecondsToFormattedTime(currentTimeState).run { "${minutes}분 ${seconds}초 남음" }.toString()
                }
            } else {
                "인증번호가 만료됐어요 다시 전송해주세요."
            }
        }
    }
    
    BottomCustomButtonLayout(
        buttonText = "인증번호 다시 받기",
        onClick = { viewModel.apply { cancelCertificationNumberCountDownTimer() ; startCertificationNumberTimer() } }
    ) {
        InputCertificationNumberContent(
            underTextValue = underTextValue,
            underTextIsPositive = currentTimeState != 0L,
            textFieldValue = textFieldStringState
        ) { textFieldStringState = it }
    }
}

@Preview(showBackground = true)
@Composable
fun InputCertificationNumberScreenPreview() {
    val viewModel: OnboardingViewModel = viewModel()
    viewModel.startCertificationNumberTimer()
    InputCertificationNumberScreen(viewModel, rememberNavController())
}