package com.smilehunter.ablebody.presentation.onboarding

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smilehunter.ablebody.presentation.login.LoginInputPhoneNumberScreen
import com.smilehunter.ablebody.presentation.onboarding.ui.CreateNicknameScreen
import com.smilehunter.ablebody.presentation.onboarding.ui.InputCertificationNumberScreen
import com.smilehunter.ablebody.presentation.onboarding.ui.InputPhoneNumberScreen
import com.smilehunter.ablebody.presentation.onboarding.ui.IntroScreen
import com.smilehunter.ablebody.presentation.onboarding.ui.SelectGenderScreen
import com.smilehunter.ablebody.presentation.onboarding.ui.SelectProfileScreen
import com.smilehunter.ablebody.presentation.onboarding.ui.WelcomeScreen


@Composable
fun OnboardingManager(viewModel: OnboardingViewModel) {
    val navController = rememberNavController()

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
            SelectGenderScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = "SelectProfile") {
            SelectProfileScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = "WelcomeScreen") {
            WelcomeScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = "LoginInputPhoneNumberScreen") {
            LoginInputPhoneNumberScreen(viewModel = viewModel, navController = navController)
        }
    }
}