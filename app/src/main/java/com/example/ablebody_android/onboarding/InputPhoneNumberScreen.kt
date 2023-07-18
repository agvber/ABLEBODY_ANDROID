package com.example.ablebody_android.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.R
import com.example.ablebody_android.utils.BottomCustomButtonLayout
import com.example.ablebody_android.utils.CustomTextField
import com.example.ablebody_android.utils.HighlightText
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.utils.TextFieldUnderCorrectText
import com.example.ablebody_android.utils.TextFieldUnderWrongText

@Composable
fun ShowPhoneNumberRule(
    value: String,
){
    if (value=="1") {
        TextFieldUnderCorrectText("사용 가능한 닉네임이에요.")
    }
    else if(value=="2"){
        TextFieldUnderWrongText("휴대폰 번호 양식에 맞지 않아요." )
    }
    else {
        TextFieldUnderCorrectText("분 초 남음")
    }
}

@Preview(showBackground = true)
@Composable
fun ShowPhoneNumberRulePreview() {
    ShowPhoneNumberRule("1")
}

@Composable
fun CheckPhoneNumberRule(
    value: String,
) {
    val path = value
    val regex = "^01[0-1, 7][0-9]{8}\$".toRegex()

    if (value == "") ShowPhoneNumberRule("")
    else{
        if (isPhoneNumberRuleMatch(path, regex)) {
            ShowPhoneNumberRule("1")
        }
        else{
            ShowPhoneNumberRule("2")
        }
    }
}

@Composable
fun isPhoneNumberRuleMatch(path: String, regex: Regex): Boolean {
    return path.matches(regex)
}

@Composable
fun InputPhoneNumberLayout(
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
fun InputPhoneNumberLayoutPreview() {
    var textState by remember { mutableStateOf("") }

    InputPhoneNumberLayout(
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
        CustomTextField(
            labelText = "휴대폰 번호", value = value, onValueChange = onValueChange,
        )
        CheckPhoneNumberRule(value)
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
private fun InputPhoneNumberContent(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
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
        InputPhoneNumberwithRuleLayout(value, onValueChange)
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentPreview() {

    var state by remember{ mutableStateOf("") }

    InputPhoneNumberContent(state) { state = it }
}


@Composable
fun InputPhoneNumberScreen() {

    var state by remember{ mutableStateOf("") }

    BottomCustomButtonLayout(
        buttonText = "인증번호 받기",
        onClick = {  }
    ) {
        InputPhoneNumberContent(state) { state = it }
    }
}

@Preview(showBackground = true)
@Composable
fun InputPhoneNumberScreenPreview() {
    InputPhoneNumberScreen()
}