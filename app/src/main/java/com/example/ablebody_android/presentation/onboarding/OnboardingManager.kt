package com.example.ablebody_android.presentation.onboarding

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ablebody_android.presentation.login.LoginInputPhoneNumberScreen
import com.example.ablebody_android.presentation.onboarding.ui.CreateNicknameScreen
import com.example.ablebody_android.presentation.onboarding.ui.InputCertificationNumberScreen
import com.example.ablebody_android.presentation.onboarding.ui.InputPhoneNumberScreen
import com.example.ablebody_android.presentation.onboarding.ui.IntroScreen
import com.example.ablebody_android.presentation.onboarding.ui.SelectGenderScreen
import com.example.ablebody_android.presentation.onboarding.ui.SelectProfileScreen
import com.example.ablebody_android.presentation.onboarding.ui.WelcomeScreen


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