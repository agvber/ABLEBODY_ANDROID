package com.example.ablebody_android.onboarding

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IntroScreen() {

    val coroutineScope = rememberCoroutineScope()

    val navController = rememberNavController()

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    NavHost(navController = navController, startDestination = "Start") {
        composable(route = "Start") {
            PermissionExplanationBottomSheet(
                sheetState = bottomSheetState,
                bottomButtonClick = { coroutineScope.launch { bottomSheetState.hide() } }
            ) {
                StartScreen { coroutineScope.launch { bottomSheetState.show() } }
            }
        }
        composable(route = "InputPhoneNumber") {
            InputPhoneNumberScreen()
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
            SelectProfileScreen()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun IntroScreenPreview() {
    IntroScreen()
}