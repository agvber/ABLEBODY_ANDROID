package com.example.ablebody_android.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.ablebody_android.utils.CustomHintTextField
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ablebody_android.ui.theme.SmallTextGrey
import com.example.ablebody_android.utils.TextFieldUnderText



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

enum class NicknameRule(val positive: Boolean, val description: String) {
    Available(true, "사용 가능한 닉네임이에요."),
    InUsed(false, "이미 사용 중인 닉네임이에요."),
    StartsWithDot(false, "닉네임은 마침표로 시작할 수 없어요."),
    OnlyNumber(false, "닉네임은 숫자로만 이뤄질 수 없어요."),
    UnAvailable(false, "사용할 수 없는 닉네임이에요."),
    Nothing(true, "20자 이내 영문, 숫자, 밑줄 및 마침표만 사용 가능해요.")
}

fun isNicknameRuleMatch(path: String, regex: Regex): Boolean = path.matches(regex)

fun checkNicknameRule(
    nickname: String,
    isNotNicknameDuplicate: Boolean?
) : NicknameRule {
    val regex1 = "[0-9a-z_.]{1,20}".toRegex()
    val regex3 = "^[.].*\$".toRegex()
    val regex4 = "^[0-9]*\$".toRegex()
//    val regex5 = "^[_]*\$".toRegex()
//    val regex6 = "^[.]*\$".toRegex()
    val regex7 = "^[._]*\$".toRegex()

    if (nickname.isEmpty()) {
        return NicknameRule.Nothing
    }
    return if (isNicknameRuleMatch(nickname, regex1)) {
        if (isNicknameRuleMatch(nickname, regex3)) {
            NicknameRule.StartsWithDot
        } else if (isNicknameRuleMatch(nickname, regex4)) {
            NicknameRule.OnlyNumber
        } else if(isNicknameRuleMatch(nickname, regex7)) {
            NicknameRule.UnAvailable
        } else {
            if (isNotNicknameDuplicate == true) {
                NicknameRule.Available
            } else {
                NicknameRule.InUsed
            }
        }
    } else {
        NicknameRule.UnAvailable
    }
}

@Composable
fun InputNicknameLayout(
    isNotNicknameDuplicate: Boolean?,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            text = "닉네임",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                fontWeight = FontWeight(400),
                color = SmallTextGrey,
            )
        )
        CustomHintTextField(
            hintText = "닉네임(20자 이내 영문,숫자,_,.가능)",
            value = value,
            onValueChange = onValueChange,
        )
        val asd = checkNicknameRule(value, isNotNicknameDuplicate)
        TextFieldUnderText(text = asd.description, isPositive = asd.positive)
    }
}

@Preview(showBackground = true)
@Composable
fun InputNicknameLayoutPreview() {
    var state by remember{ mutableStateOf("") }
    InputNicknameLayout(
        isNotNicknameDuplicate = true,
        value = state,
        onValueChange = { state = it }
    )
}

@Composable
fun CreateNicknameScreen(
    viewModel: OnboardingViewModel = viewModel()
) {
    var nicknameState by remember { mutableStateOf("") }
    var phoneNumberState by remember { mutableStateOf("") }

    val isNotNicknameDuplicate by viewModel.isNotNicknameDuplicate.observeAsState()

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
            InputNicknameLayout(
                isNotNicknameDuplicate = isNotNicknameDuplicate,
                value = nicknameState,
            ) {
                if (it.isNotEmpty()) viewModel.checkDuplicateNickname(it)
                nicknameState = it
            }
            InputPhoneNumberLayout(phoneNumberState) { phoneNumberState = it }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CreateNicknameScreenPreview() {
    CreateNicknameScreen()
}