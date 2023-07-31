package com.example.ablebody_android.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ablebody_android.R
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.SmallTextGrey
import com.example.ablebody_android.utils.HighlightText

@Composable
fun WelcomeScreen(viewModel: OnboardingViewModel) {
    val gender by viewModel.genderState.collectAsStateWithLifecycle()
    val nickname by viewModel.nicknameState.collectAsStateWithLifecycle()
    val profileImage by viewModel.profileImageState.collectAsStateWithLifecycle()

    viewModel.createNewUser(
        gender = gender!!,
        nickname = nickname,
        profileImage = profileImage!!.resourcesID,
        verifyingCode = viewModel.toString(),
        agreeRequiredConsent = false,
        agreeMarketingConsent = false
    ).runCatching {

    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "로그인 완료!",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                fontWeight = FontWeight(400),
                color = SmallTextGrey,
                textAlign = TextAlign.Center,
            )
        )

        HighlightText(
            string = "${nickname}님,\n환영합니다!",
            colorStringList = listOf(nickname),
            color = AbleBlue,
            style = TextStyle(
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                fontWeight = FontWeight(700),
                color = AbleDark,
                textAlign = TextAlign.Center,
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    val viewModel: OnboardingViewModel = viewModel()
    WelcomeScreen(viewModel = viewModel)
}