package com.example.ablebody_android.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ablebody_android.login.LoginInputPhoneNumberScreen


@Composable
fun OnboardingManager(viewModel: OnboardingViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
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

@Preview(showSystemUi = true)
@Composable
fun OnboardingManagerPreview() {
    OnboardingManager()
}