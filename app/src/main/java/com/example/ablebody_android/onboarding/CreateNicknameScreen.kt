package com.example.ablebody_android.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ablebody_android.onboarding.data.NicknameRule
import com.example.ablebody_android.utils.DisableCustomTextField
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

@Composable
fun InputNicknamewithRuleLayout(
    nicknameRule: () -> NicknameRule,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        CustomTextField(
            labelText = "닉네임",
            value = value,
            onValueChange = onValueChange,
        )
        TextFieldUnderText(text = nicknameRule().description, isPositive = nicknameRule().positive)
    }
}

@Preview(showBackground = true)
@Composable
fun InputNicknameLayoutPreview() {
    var state by remember{ mutableStateOf("") }
    InputNicknamewithRuleLayout(
        { NicknameRule.Nothing },
        value = state,
        onValueChange = { state = it }
    )
}

@Composable
fun CreateNicknameScreen(
    viewModel: OnboardingViewModel = viewModel(),
    phoneNumber : String
) {
    var nicknameState by remember { mutableStateOf("") }

    val viewModelNicknameState by viewModel.availableNicknameCheckLiveData.observeAsState()

    BottomCustomButtonLayout(
        buttonText = "확인",
        onClick = {  }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
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
            InputNicknamewithRuleLayout(
                nicknameRule = {
                    viewModelNicknameState ?: NicknameRule.Nothing
                },
                value = nicknameState,
            ) {
                nicknameState = it
                viewModel.checkAvailableNickname(nicknameState)
            }
            //생각해보니 텍스트 필드 아래 글자가 없는 건 InputPhoneNumberWithoutRuleLayout이 아니라
            // 그냥 CustomTextField를 쓰면 되겠구나..
            DisableCustomTextField(value = phoneNumber!!){}
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CreateNicknameScreenPreview() {
    val viewModel: OnboardingViewModel = viewModel()
    CreateNicknameScreen(viewModel,"01092393487")
}