package com.smilehunter.ablebody.presentation.my.myInfo.ui

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.my.myInfo.MyInfoEditViewModel
import com.smilehunter.ablebody.presentation.onboarding.ui.InputPhoneNumberWithRuleLayout
import com.smilehunter.ablebody.presentation.onboarding.OnboardingViewModel
import com.smilehunter.ablebody.presentation.onboarding.data.CertificationNumberInfoMessageUiState
import com.smilehunter.ablebody.presentation.onboarding.ui.InputCertificationNumberContent
import com.smilehunter.ablebody.presentation.onboarding.ui.InputCertificationNumberTextField
import com.smilehunter.ablebody.presentation.onboarding.ui.InputPhoneNumberWithoutRuleLayout
import com.smilehunter.ablebody.ui.utils.BottomCustomButtonLayout
import com.smilehunter.ablebody.ui.utils.HighlightText
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.utils.CustomLabelText
import com.smilehunter.ablebody.ui.utils.CustomTextField
import com.smilehunter.ablebody.ui.utils.SimpleErrorHandler
import com.smilehunter.ablebody.ui.utils.TextFieldUnderNormalText
import com.smilehunter.ablebody.ui.utils.TextFieldUnderText
import kotlinx.coroutines.flow.launchIn

@Composable
fun ChangePhoneNumberRoute(
    myInfoEditViewModel: MyInfoEditViewModel = hiltViewModel(),
    certificationBtnOnClick: (String) -> Unit
) {
    val phoneNumberState by myInfoEditViewModel.phoneNumberState.collectAsStateWithLifecycle()
    val phoneNumberEnable by myInfoEditViewModel.isPhoneNumberCorrectState.collectAsStateWithLifecycle()
    val errorData by myInfoEditViewModel.sendErrorLiveData.observeAsState()

    Log.d("phoneNumberState", phoneNumberState)
    ChangePhoneNumberScreen(
        onPhoneNumberChange = {
            Log.d("phoneNumber",it)
            myInfoEditViewModel.updatePhoneNumber(it)
        },
        phoneNumberEnable = phoneNumberEnable,
        phoneNumber = phoneNumberState,
        certificationBtnOnClick = {
            certificationBtnOnClick(phoneNumberState)
        }
    )

    if (errorData != null) {
        val context = LocalContext.current
        Toast.makeText(context, "네트워크 에러가 발생했습니다\n다시 시도해주세요", Toast.LENGTH_LONG).show()
    }
}

@Composable
fun ChangePhoneNumberScreen(
    onPhoneNumberChange:(String) -> Unit,
    phoneNumberEnable: Boolean,
    phoneNumber: String,
    certificationBtnOnClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 100.dp),
    ){
        ChangePhoneNumberExplanation()
        InputPhoneNumberWithRuleLayout(
            value = phoneNumber,
            onValueChange = {
                onPhoneNumberChange(it)
            },
            underText = if(phoneNumber.isNotEmpty() && !phoneNumberEnable) "휴대폰 번호 양식에 맞지 않아요." else ""
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = {
                    Log.d("인증번호 받기 버튼 눌림","인증번호 받기 버튼 눌림")
                    certificationBtnOnClick()
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AbleBlue),
                enabled = phoneNumberEnable
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
    ChangePhoneNumberScreen({}, true, "", {})
}