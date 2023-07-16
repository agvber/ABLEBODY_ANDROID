package com.example.ablebody_android.onboarding

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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
import com.example.ablebody_android.R
import com.example.ablebody_android.utils.BottomCustomButtonLayout
import com.example.ablebody_android.utils.CustomTextField
import com.example.ablebody_android.utils.HighlightText
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.AbleRed
import com.example.ablebody_android.utils.TextFieldUnderCorrectText
import com.example.ablebody_android.utils.TextFieldUnderWrongText

@Composable
fun ShowNicknameRule(
    value: String,
){
    if (value=="1") {
        TextFieldUnderCorrectText("사용 가능한 닉네임이에요.")
    }
    else if (value=="2") {
        TextFieldUnderWrongText("이미 사용 중인 닉네임이에요.")
    }
    else if (value=="3") {
        TextFieldUnderWrongText("닉네임은 마침표로 시작할 수 없어요.")
    }
    else if (value=="4") {
        TextFieldUnderWrongText("닉네임은 숫자로만 이뤄질 수 없어요.")
    }
    else if (value=="5") {
        TextFieldUnderWrongText("사용할 수 없는 닉네임이에요.")
    }
    else {
        TextFieldUnderCorrectText("20자 이내 영문, 숫자, 밑줄 및 마침표만 사용 가능해요.")
    }
}

@Preview(showBackground = true)
@Composable
fun ShowNicknameRulePreview() {
    ShowNicknameRule("1")
}

@Composable
fun InputNicknameLayout(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        CustomTextField(
            labelText = "닉네임", value = value, onValueChange = onValueChange
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InputNicknameLayoutPreview() {

    var state by remember{ mutableStateOf("") }
    InputNicknameLayout(state) { state = it }
}

@Composable
fun InputNicknamewithRuleLayout(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        CustomTextField(
            labelText = "닉네임", value = value, onValueChange = onValueChange
        )
        CheckNicknameRule(value)
    }

}

@Preview(showBackground = true)
@Composable
fun InputNicknamewithRuleLayoutPreview() {
    var textState by remember { mutableStateOf("") }

    InputNicknamewithRuleLayout(
        value = textState,
        onValueChange = { textState = it }
    )
}

@Composable
fun CheckNicknameRule(
    value: String,
) {
    val path = value
    val regex1 = "[0-9a-z_.]{1,20}".toRegex()
    val regex3 = "^[.].*\$".toRegex()
    val regex4 = "^[0-9]*\$".toRegex()
    val regex5 = "^[_]*\$".toRegex()
    val regex6 = "^[.]*\$".toRegex()
    val regex7 = "^[._]*\$".toRegex()

    if (value == "") ShowNicknameRule("")
    else{
        if (isNicknameRuleMatch(path, regex1)) {
            if(isNicknameRuleMatch(path, regex3)){
                ShowNicknameRule("3")
            }else if(isNicknameRuleMatch(path, regex4)){
                ShowNicknameRule("4")
            }else if(isNicknameRuleMatch(path, regex7)){
                ShowNicknameRule("5")
            }
            else {
//                if(/*TODO : 서버에서 중복된 닉네임 확인*/)
//                else
                ShowNicknameRule("1")
            }
        }
        else{
            ShowNicknameRule("5")
        }
    }
}

@Composable
fun isNicknameRuleMatch(path: String, regex: Regex): Boolean {
    return path.matches(regex)
}

@Composable
fun CreateNicknameScreen() {

    var state by remember{ mutableStateOf("") }

    BottomCustomButtonLayout(
        buttonText = "확인",
        onClick = {  }
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            HighlightText(
                string = "닉네임을 입력해주세요",
                colorStringList = listOf("닉네임"),
                color = AbleBlue,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 22.sp,
                    lineHeight = 35.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                    fontWeight = FontWeight(700),
                    color = AbleDark,
                )
            )
            InputNicknamewithRuleLayout(state) { state = it }
            InputPhoneNumberLayout(value = "01012345678") {  }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CreateNicknameScreenPreview() {
    CreateNicknameScreen()
}