package com.smilehunter.ablebody.presentation.onboarding.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.presentation.onboarding.OnboardingViewModel
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.theme.White
import com.smilehunter.ablebody.ui.utils.BottomCustomButtonLayout
import com.smilehunter.ablebody.ui.utils.DisableCustomWithLabelTextField
import com.smilehunter.ablebody.ui.utils.HighlightText


@Composable
fun SelectGenderButton(
    text: String,
    isChecked: Boolean,
    onClick: () -> Unit
) {
    val buttonAnimatedColor by animateColorAsState(
        targetValue = if (isChecked) AbleBlue else PlaneGrey,
        animationSpec = tween(200)
    )
    val textAnimatedColor by animateColorAsState(
        targetValue = if (isChecked) White else SmallTextGrey,
        animationSpec = tween(100)
    )

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonAnimatedColor
        ),
        modifier = Modifier.padding(5.dp),
        shape = RoundedCornerShape(5.dp),
        onClick = onClick
    ) {
        Text(text = text, color = textAnimatedColor)
    }
}

@Preview(showBackground = true)
@Composable
fun SelectGenderButtonPreview() {
    SelectGenderButton("남자", false) {}
}

@Composable
fun SelectGenderLayout(
    gender: Gender?,
    onClick: (Gender) -> Unit
) {
    Column {
        Text(
            text = "성별",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_black)),
                fontWeight = FontWeight(400),
                color = SmallTextGrey,
            ),
            modifier = Modifier.padding(top=15.dp)
        )
        
        LazyRow {
            items(Gender.values()) {
                SelectGenderButton(
                    text = stringResource(id = it.resourceID),
                    isChecked = gender == it,
                    onClick = { onClick(it) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectGenderLayoutPreview() {
    var gender by remember { mutableStateOf<Gender>(Gender.MALE) }

    SelectGenderLayout(gender = gender) {
        gender = it
    }
}


@Composable
fun SelectGenderScreen(
    viewModel: OnboardingViewModel,
    navController: NavController
) {
    val nickname by viewModel.nicknameState.collectAsStateWithLifecycle()
    val phoneNumber by viewModel.phoneNumberState.collectAsStateWithLifecycle()
    val gender by viewModel.genderState.collectAsStateWithLifecycle()

    BottomCustomButtonLayout(
        buttonText = "확인",
        enable = gender != null,
        onClick = { navController.navigate("SelectProfile") }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            HighlightText(
                string = "성별을 입력해주세요",
                colorStringList = listOf("성별"),
                color = AbleBlue,
                style = TextStyle(
                    fontSize = 22.sp,
                    lineHeight = 35.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_black)),
                    fontWeight = FontWeight(700),
                    color = AbleDark,
                )
            )
            SelectGenderLayout(gender = gender, onClick = { viewModel.updateGender(it) })
//            InputNicknameLayout(nickname)  {  }
            DisableCustomWithLabelTextField(
                value = nickname,
                onValueChange = {  },
                labelText = { androidx.compose.material3.Text(text = "닉네임") }
            )
            DisableCustomWithLabelTextField(
                value = phoneNumber,
                onValueChange = {  },
                labelText = { androidx.compose.material3.Text(text = "휴대폰 번호") }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SelectGenderScreenPreview() {
    val viewModel: OnboardingViewModel = viewModel()
    val navController = rememberNavController()
    SelectGenderScreen(viewModel = viewModel, navController = navController)
}