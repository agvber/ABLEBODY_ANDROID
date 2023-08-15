package com.example.ablebody_android.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ablebody_android.R
import com.example.ablebody_android.onboarding.InputPhoneNumberWithRuleLayout
import com.example.ablebody_android.onboarding.OnboardingViewModel
import com.example.ablebody_android.utils.BottomCustomButtonLayout
import com.example.ablebody_android.utils.HighlightText
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import kotlinx.coroutines.flow.launchIn

@Composable
fun LoginPhoneNumberJoinExplanation() {
    HighlightText(
        string = "휴대폰 번호(아이디)를 입력해주세요.",
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

@Preview(showBackground = true)
@Composable
fun LoginPhoneNumberJoinExplanationPreview() {
    LoginPhoneNumberJoinExplanation()
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
@Composable
fun LoginInputPhoneNumberContent(
    value: String,
    onValueChange: (String) -> Unit,
    underText: String
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//        TopBarBackward()
        LoginPhoneNumberJoinExplanation()
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

@Preview(showBackground = true)
@Composable
private fun ContentPreview() {

    var textState by remember { mutableStateOf("") }

    InputPhoneNumberWithRuleLayout(
        value = textState,
        onValueChange = { textState = it },
        underText = "휴대폰 번호 양식에 맞지 않아요."
    )
}


@Composable
fun LoginInputPhoneNumberScreen(
    viewModel: OnboardingViewModel,
    navController: NavController
) {

    val phoneNumber by viewModel.phoneNumberState.collectAsStateWithLifecycle()
    val enable by viewModel.isPhoneNumberCorrectState.collectAsStateWithLifecycle()
    val phoneNumberMessage by viewModel.phoneNumberMessageStateUi.collectAsStateWithLifecycle()

//    TopBarBackward()
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
fun LoginInputPhoneNumberScreenPreview() {
    val viewModel: OnboardingViewModel = viewModel()
    val navController = rememberNavController()
    LoginInputPhoneNumberScreen(viewModel = viewModel, navController = navController)
}