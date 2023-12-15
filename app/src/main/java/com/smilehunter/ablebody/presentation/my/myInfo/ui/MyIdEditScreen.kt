package com.smilehunter.ablebody.presentation.my.myInfo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.presentation.onboarding.ui.InputPhoneNumberWithRuleLayout
import com.smilehunter.ablebody.presentation.onboarding.OnboardingViewModel
import com.smilehunter.ablebody.presentation.onboarding.ui.InputPhoneNumberWithoutRuleLayout
import com.smilehunter.ablebody.ui.utils.BottomCustomButtonLayout
import com.smilehunter.ablebody.ui.utils.HighlightText
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import kotlinx.coroutines.flow.launchIn


@Composable
fun ChangePhoneNumberScreen() {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 100.dp),
    ){
        ChangePhoneNumberExplanation()
//        InputPhoneNumberWithoutRuleLayout(
//            value = "",
//            onValueChange = {},
//            enable = true
//        )
        InputPhoneNumberWithRuleLayout(
            value = "",
            onValueChange = {},
            underText = "휴대폰 번호 양식에 맞지 않아요."
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = {
//                    editSavePopup = true
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AbleBlue),
                enabled = false
            ) {
                Text(
                    text = "인증번호 받기",
                    color = Color.White
                )
            }
        }
    }
}
@Composable
fun ChangePhoneNumberExplanation() {
    HighlightText(
        string = "휴대폰 번호(아이디)를 변경할게요.",
        colorStringList = listOf("휴대폰 번호(아이디)"),
        color = AbleBlue,
        style = TextStyle(
            fontSize = 22.sp,
            lineHeight = 35.sp,
            fontWeight = FontWeight(700),
            color = AbleDark,
            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_black))
        )
    )
}


@Composable
fun LoginInputPhoneNumberContent(
    value: String,
    onValueChange: (String) -> Unit,
    underText: String
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        ChangePhoneNumberExplanation()
        InputPhoneNumberWithRuleLayout(value, onValueChange, underText)
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

@Composable
fun LoginInputPhoneNumberScreen(
    viewModel: OnboardingViewModel,
    navController: NavController
) {

    val phoneNumber by viewModel.phoneNumberState.collectAsStateWithLifecycle()
    val enable by viewModel.isPhoneNumberCorrectState.collectAsStateWithLifecycle()
    val phoneNumberMessage by viewModel.phoneNumberMessageStateUi.collectAsStateWithLifecycle()

    BottomCustomButtonLayout(
        buttonText = "인증번호 받기",
        onClick = {
            viewModel.requestSmsVerificationCode(phoneNumber)
            viewModel.startCertificationNumberTimer()
            navController.navigate(route = "InputCertificationNumber")
        },
        enable = enable
    ) {
        LoginInputPhoneNumberContent(
            value = phoneNumber,
            onValueChange = { viewModel.updatePhoneNumber(it) },
            underText = phoneNumberMessage
        )
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.updateCertificationNumber("")
        viewModel.certificationNumberInfoMessageUiState.launchIn(viewModel.viewModelScope)
    }
}
@Preview(showBackground = true)
@Composable
fun LoginPhoneNumberJoinExplanationPreview() {
    ChangePhoneNumberExplanation()
}

@Preview(showBackground = true)
@Composable
fun InputPhoneNumberWithRuleLayoutPreview() {
    var textState by remember { mutableStateOf("") }

    InputPhoneNumberWithRuleLayout(
        value = textState,
        onValueChange = { textState = it },
        underText = "휴대폰 번호 양식에 맞지 않아요."
    )
}

@Preview(showSystemUi = true)
@Composable
fun ChangePhoneNumberScreenPreview() {
    ChangePhoneNumberScreen()
}