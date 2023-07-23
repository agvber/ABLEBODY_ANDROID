package com.example.ablebody_android.onboarding

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.R
import com.example.ablebody_android.utils.BottomCustomButtonLayout
import com.example.ablebody_android.utils.CustomTextField
import com.example.ablebody_android.utils.HighlightText
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.utils.TextFieldUnderCorrectText
import com.example.ablebody_android.utils.TextFieldUnderText
import com.example.ablebody_android.utils.TextFieldUnderWrongText
@Composable
fun InputPhoneNumberwithoutRuleLayout(
    value: String,
    onValueChange: (String) -> Unit
) {
    CustomTextField(
        labelText = "휴대폰 번호",
        value = value,
        onValueChange = onValueChange
    )
}

@Preview(showBackground = true)
@Composable
fun InputPhoneNumberwithoutRuleLayoutPreview() {
    var textState by remember { mutableStateOf("") }

    InputPhoneNumberwithoutRuleLayout(
        value = textState,
        onValueChange = { textState = it }
    )
}

@Composable
fun InputPhoneNumberwithRuleLayout(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        InputPhoneNumberwithoutRuleLayout(value, onValueChange)
        PhoneNumberFormRule(value)
    }

}
@Preview(showBackground = true)
@Composable
fun InputPhoneNumberwithRuleLayoutPreview() {
    var textState by remember { mutableStateOf("") }

    InputPhoneNumberwithRuleLayout(
        value = textState,
        onValueChange = { textState = it }
    )
}
@Composable
fun PhoneNumberFormRule(
    value: String,
) {
    val path = value
    val regex = "^01[0-1, 7][0-9]{8}\$".toRegex()

    if (value == "") {
        TextFieldUnderText("분 초 남음", true)
    }
    else{
        if (isPhoneNumberRuleMatch(path, regex)) {
            TextFieldUnderText("", true)
        }
        else{
            TextFieldUnderText("휴대폰 번호 양식에 맞지 않아요.", false)
        }
    }
}
@Composable
fun isPhoneNumberRuleMatch(path: String, regex: Regex): Boolean = path.matches(regex)

@Composable
fun PhonenumberFormJudgment(
    phonenumber: String,
) : Boolean{

    val path = phonenumber
    val regex = "^01[0-1, 7][0-9]{8}\$".toRegex()

    return !(phonenumber=="" || !isPhoneNumberRuleMatch(path, regex))
}

@Composable
fun PhonenumberJoinExplanation(){
    HighlightText(
        string = "애블바디는 휴대폰 번호로 가입해요.\n휴대폰 번호는 안전하게 보관되며\n어디에도 공개되지 않아요.",
        colorStringList = listOf("휴대폰 번호"),
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

@Composable
private fun InputPhoneNumberContent(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        PhonenumberJoinExplanation()
        InputPhoneNumberwithRuleLayout(value, onValueChange)
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentPreview() {

    var phoneNumberState by remember{ mutableStateOf("") }
    InputPhoneNumberContent(phoneNumberState) { phoneNumberState = it }

}


@Composable
fun InputPhoneNumberScreen(
    viewModel: OnboardingViewModel = viewModel()
) {
    var phoneNumberState by remember{ mutableStateOf("") }
//    val isNotPhonenumberDuplicate by viewModel.isNotPhonenumberDuplicate.observeAsState()

    BottomCustomButtonLayout(
        buttonText = "인증번호 받기",
        onClick = {
            viewModel.checkDuplicatePhonenumber(phoneNumberState)
        },
        enable = PhonenumberFormJudgment(phoneNumberState)
    ) {
        InputPhoneNumberContent(phoneNumberState) { phoneNumberState = it }
    }
}

@Preview(showBackground = true)
@Composable
fun InputPhoneNumberScreenPreview() {
    InputPhoneNumberScreen()
}