package com.example.ablebody_android.onboarding

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ablebody_android.onboarding.utils.checkPermission
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OnboardingManager(viewModel: OnboardingViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val coroutineScope = rememberCoroutineScope()

    val navController = rememberNavController()

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "Start") {
        composable(route = "Start") {

            val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

            }

            PermissionExplanationBottomSheet(
                sheetState = bottomSheetState,
                bottomButtonClick = {
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }
                    launcher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_MEDIA_IMAGES))
                    navController.navigate("InputPhoneNumber")
                }
            ) {
                IntroScreen { coroutineScope.launch {
                    if (
                        !checkPermission(context, Manifest.permission.POST_NOTIFICATIONS) ||
                        !checkPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
                    ) {
                        bottomSheetState.show()
                    } else {
                        navController.navigate("InputPhoneNumber")
                    }
                } }
            }
        }
        composable(route = "InputPhoneNumber") {
            InputPhoneNumberScreen(viewModel,navController)
        }
        composable(route = "InputCertificationNumber") {
            InputCertificationNumberScreen(viewModel, navController)
        }
        composable(route = "CreateNickname") {
            CreateNicknameScreen()
        }
        composable(route = "InputGender") {
            InputGenderScreen(
                "nickname",
                "01026289219",
                gender = null,
                onClick = {  }
            )
        }
        composable(route = "SelectProfile") {
            SelectProfileScreen(null) {  }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun OnboardingManagerPreview() {
    OnboardingManager()
}