package com.example.ablebody_android.onboarding

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
import com.example.ablebody_android.onboarding.data.NicknameRule
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.utils.BottomCustomButtonLayout
import com.example.ablebody_android.utils.CustomTextField
import com.example.ablebody_android.utils.DisableCustomWithLabelTextField
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
    nicknameText: String,
    nicknameTextChange: (String) -> Unit,
    nicknameRuleState: NicknameRule,
    phoneNumber : String,
    bottomButtonOnClick: () -> Unit
) {
    BottomCustomButtonLayout(
        buttonText = "확인",
        onClick = bottomButtonOnClick
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
                onValueChange = nicknameTextChange
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
    var nicknameTextState by remember { mutableStateOf("") }
    CreateNicknameScreen(
        phoneNumber = "01026289219",
        nicknameText = nicknameTextState,
        nicknameRuleState = NicknameRule.Nothing,
        nicknameTextChange = { nicknameTextState = it }
    ){}
}