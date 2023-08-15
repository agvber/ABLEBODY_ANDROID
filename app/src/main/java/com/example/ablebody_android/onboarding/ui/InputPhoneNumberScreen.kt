package com.example.ablebody_android.onboarding.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ablebody_android.R
import com.example.ablebody_android.onboarding.OnboardingViewModel
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.utils.BottomCustomButtonLayout
import com.example.ablebody_android.utils.CustomLabelText
import com.example.ablebody_android.utils.CustomTextField
import com.example.ablebody_android.utils.HighlightText
import com.example.ablebody_android.utils.TextFieldUnderText
import kotlinx.coroutines.flow.launchIn

@Composable
fun PhoneNumberJoinExplanation() {
    HighlightText(
        string = "애블바디는 휴대폰 번호로 가입해요.\n휴대폰 번호는 안전하게 보관되며\n어디에도 공개되지 않아요.",
        colorStringList = listOf("휴대폰 번호"),
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
fun PhoneNumberJoinExplanationPreview() {
    PhoneNumberJoinExplanation()
}


@Composable
fun InputPhoneNumberWithoutRuleLayout(
    value: String,
    onValueChange: (String) -> Unit,
    enable: Boolean = true,
) {
    CustomTextField(
        labelText = { CustomLabelText(text = "휴대폰 번호") },
        value = value,
        onValueChange = onValueChange,
        enabled = enable,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Preview(showBackground = true)
@Composable
fun InputPhoneNumberWithoutRuleLayoutPreview() {
    var textState by remember { mutableStateOf("") }

    InputPhoneNumberWithoutRuleLayout(
        value = textState,
        onValueChange = { textState = it }
    )
}

@Composable
fun InputPhoneNumberWithRuleLayout(
    value: String,
    onValueChange: (String) -> Unit,
    underText: String
) {
    Column {
        InputPhoneNumberWithoutRuleLayout(value, onValueChange)
        TextFieldUnderText(underText, false)
    }
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
private fun InputPhoneNumberContent(
    value: String,
    onValueChange: (String) -> Unit,
    underText: String,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        PhoneNumberJoinExplanation()
        InputPhoneNumberWithRuleLayout(value, onValueChange, underText)
    }
}

@Preview(showBackground = true)
@Composable
private fun InputCertificationNumberContentPreview() {
    var phoneNumberState by remember{ mutableStateOf("") }
    InputPhoneNumberContent(
        value = phoneNumberState,
        onValueChange = { phoneNumberState = it },
        underText = "휴대폰 번호 양식에 맞지 않아요."
    )
}


@Composable
fun InputPhoneNumberScreen(
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
        InputPhoneNumberContent(
            value = phoneNumber,
            onValueChange = { viewModel.updatePhoneNumber(it) },
            underText = phoneNumberMessage
        )
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.updateCertificationNumber("")
        viewModel.verificationResultState.launchIn(viewModel.viewModelScope)
        viewModel.certificationNumberInfoMessageUiState.launchIn(viewModel.viewModelScope)
    }
}

@Preview(showBackground = true)
@Composable
fun InputPhoneNumberScreenPreview() {
    val viewModel: OnboardingViewModel = viewModel()
    val navController = rememberNavController()
    InputPhoneNumberScreen(viewModel = viewModel, navController = navController)
}