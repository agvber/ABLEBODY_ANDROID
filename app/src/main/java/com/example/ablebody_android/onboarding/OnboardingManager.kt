package com.example.ablebody_android.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ablebody_android.Gender
import com.example.ablebody_android.onboarding.data.ProfileImages


@Composable
fun OnboardingManager(viewModel: OnboardingViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val navController = rememberNavController()
    val phoneNumberState by viewModel.phoneNumberState.collectAsStateWithLifecycle()
    val nicknameTextState by remember { mutableStateOf("") }
    var genderState by remember { mutableStateOf<Gender?>(null) }
    var profileImageState by remember { mutableStateOf<ProfileImages?>(null) }

    NavHost(navController = navController, startDestination = "Start") {
        composable(route = "Start") {
            IntroScreen(navController = navController)
        }
        composable(route = "InputPhoneNumber") {
            InputPhoneNumberScreen(viewModel = viewModel, navController = navController)
        }
        composable("InputCertificationNumber") {
            InputCertificationNumberScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = "CreateNickname") {
            CreateNicknameScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = "InputGender") {
            SelectGenderScreen(
                gender = genderState,
                genderOnChange = { genderState = it },
                phoneNumber = phoneNumberState,
                nickname = nicknameTextState,
                bottomButtonOnClick = { navController.navigate("SelectProfile") }
            )
        }
        composable(route = "SelectProfile") {
            SelectProfileScreen(
                value = profileImageState,
                onChangeValue = { profileImageState = it },
                bottomButtonOnClick = { navController.navigate("WelcomeScreen") }
            )
        }
        composable(route = "WelcomeScreen") {
            WelcomeScreen(
                nickname = nicknameTextState
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun OnboardingManagerPreview() {
    OnboardingManager()
}