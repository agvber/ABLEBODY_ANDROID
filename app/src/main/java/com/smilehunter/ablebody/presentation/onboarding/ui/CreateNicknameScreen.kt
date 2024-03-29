package com.smilehunter.ablebody.presentation.onboarding.ui

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.presentation.onboarding.OnboardingViewModel
import com.smilehunter.ablebody.presentation.onboarding.data.NicknameRule
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.utils.BottomCustomButtonLayout
import com.smilehunter.ablebody.ui.utils.CustomTextField
import com.smilehunter.ablebody.ui.utils.DisableCustomWithLabelTextField
import com.smilehunter.ablebody.ui.utils.HighlightText
import com.smilehunter.ablebody.ui.utils.TextFieldUnderText

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
        onClick = { navController.navigate("InputGender") },
        enable = NicknameRule.Available == nicknameRuleState
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
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_black)),
                    fontWeight = FontWeight(700),
                    color = AbleDark,
                )
            )
            InputNicknameWithRuleLayout(
                nicknameRule = nicknameRuleState,
                value = nicknameText,
                onValueChange = { viewModel.updateNickname(it) }
            )

            DisableCustomWithLabelTextField(
                value = phoneNumber,
                onValueChange = {  },
                labelText = { Text(text = "휴대폰 번호") }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CreateNicknameScreenPreview() {
    val viewModel: OnboardingViewModel = viewModel()
    val navController = rememberNavController()
    CreateNicknameScreen(viewModel = viewModel, navController = navController)
}