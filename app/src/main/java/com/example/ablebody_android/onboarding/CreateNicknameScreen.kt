package com.example.ablebody_android.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ablebody_android.R
import com.example.ablebody_android.onboarding.data.NicknameRule
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.utils.BottomCustomButtonLayout
import com.example.ablebody_android.utils.CustomTextField
import com.example.ablebody_android.utils.HighlightText
import com.example.ablebody_android.utils.TextFieldUnderText

@Composable
fun InputNicknameLayout(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        CustomTextField(
            value = value,
            onValueChange = onValueChange,
            labelText = { Text(text = "닉네임(20자 이내 영문,숫자,_,.가능") },
        )
    }
}

@Composable
fun InputNicknameWithRuleLayout(
    nicknameRule: NicknameRule,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        CustomTextField(
            labelText = { Text(text = "닉네임(20자 이내 영문,숫자,_,.가능") },
            value = value,
            onValueChange = onValueChange,
        )
        TextFieldUnderText(text = nicknameRule.description, isPositive = nicknameRule.positive)
    }
}

@Preview(showBackground = true)
@Composable
fun InputNicknameLayoutPreview() {
    var state by remember{ mutableStateOf("") }
    InputNicknameWithRuleLayout(
        NicknameRule.Nothing,
        value = state,
        onValueChange = { state = it }
    )
}

@Composable
fun CreateNicknameScreen(
    viewModel: OnboardingViewModel,
    navController: NavController
) {
    val phoneNumber by viewModel.phoneNumberState.collectAsStateWithLifecycle()
    val nicknameText by viewModel.nicknameState.collectAsStateWithLifecycle()
    val nicknameRuleState by viewModel.nicknameRuleState.collectAsStateWithLifecycle()

    BottomCustomButtonLayout(
        buttonText = "확인",
        onClick = { navController.navigate("InputGender") }
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
            InputNicknameWithRuleLayout(
                nicknameRule = nicknameRuleState,
                value = nicknameText,
                onValueChange = { viewModel.updateNickname(it) }
            )
            InputPhoneNumberWithoutRuleLayout(
                value = phoneNumber,
                onValueChange = {},
                enable = false,
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CreateNicknameScreenPreview() {
    val viewModel: OnboardingViewModel = viewModel()
    CreateNicknameScreen(
        viewModel = viewModel,
        navController = rememberNavController()
    )
}