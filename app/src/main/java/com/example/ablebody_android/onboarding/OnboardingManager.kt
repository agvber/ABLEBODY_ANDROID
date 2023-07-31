package com.example.ablebody_android.onboarding

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ablebody_android.Gender
import com.example.ablebody_android.onboarding.data.CertificationNumberInfoMessageUiState
import com.example.ablebody_android.onboarding.data.NicknameRule
import com.example.ablebody_android.onboarding.data.ProfileImages
import com.example.ablebody_android.onboarding.utils.checkPermission
import kotlinx.coroutines.launch


@Composable
fun OnboardingManager(viewModel: OnboardingViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val navController = rememberNavController()
    val phoneNumberState by viewModel.phoneNumberState.collectAsStateWithLifecycle()
    var nicknameTextState by remember { mutableStateOf("") }
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
            InputGenderScreen(
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
            WelcomeScreen()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun OnboardingManagerPreview() {
    OnboardingManager()
}