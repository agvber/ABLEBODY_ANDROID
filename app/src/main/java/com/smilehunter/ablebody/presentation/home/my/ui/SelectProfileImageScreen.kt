package com.smilehunter.ablebody.presentation.home.my.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smilehunter.ablebody.presentation.onboarding.data.ProfileImages
import com.smilehunter.ablebody.presentation.onboarding.ui.SelectProfileImageLayout
import com.smilehunter.ablebody.presentation.onboarding.ui.SelectProfileTitleLayout
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.CustomButton

@Composable
fun SelectProfileImageScreen(
    onBackRequest: () -> Unit,
    confirmButtonClick: (Int) -> Unit
) {
    var profileImageNumber by rememberSaveable { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
             BackButtonTopBarLayout(
                 onBackRequest = onBackRequest,
                 titleText = "프로필 사진"
             )
        },
        bottomBar = {
            CustomButton(
                text = "확인",
                enable = profileImageNumber != null
            ) {
                profileImageNumber?.let(confirmButtonClick)
            }
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
                .padding(horizontal = 16.dp)
                .padding(top = 42.dp)
        ) {
            SelectProfileTitleLayout()
            SelectProfileImageLayout(
                value = profileImageNumber?.let { ProfileImages.values()[it] },
                onChangeValue = { profileImageNumber = it.ordinal }
            )
        }
    }
}

@Preview
@Composable
fun SelectProfileImageScreenPreview() {
    ABLEBODY_AndroidTheme {
        SelectProfileImageScreen(
            onBackRequest = {},
            confirmButtonClick = {}
        )
    }
}