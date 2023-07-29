package com.example.ablebody_android.onboarding

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ablebody_android.onboarding.utils.checkPermission
import com.example.ablebody_android.onboarding.utils.convertMillisecondsToFormattedTime
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

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}

    var phoneNumberState by remember { mutableStateOf("") }

    NavHost(navController = navController, startDestination = "Start") {
        composable(route = "Start") {
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
            InputPhoneNumberScreen(value = phoneNumberState, onValueChange = { phoneNumberState = it }) {
                viewModel.sendSMS(phoneNumberState)
                viewModel.startCertificationNumberTimer()
                navController.navigate("InputCertificationNumber")
            }
        }


        composable("InputCertificationNumber") {
            var certificationNumberState by remember { mutableStateOf("") }
            val currentCertificationNumberTimeLiveData by viewModel.currentCertificationNumberTimeLiveData.observeAsState()
            val sendSMSLiveData by viewModel.sendSMSLiveData.observeAsState()
            val checkSMSLiveData by viewModel.checkSMSLiveData.observeAsState()

            if (checkSMSLiveData?.body()?.success == true) {
                navController.navigate("CreateNickname")
            }

            val underText: String by remember {
                derivedStateOf {
                    Log.d("TEST", checkSMSLiveData.toString())
                    if (currentCertificationNumberTimeLiveData == 0L) {
                        "인증번호가 만료됐어요 다시 전송해주세요."
                    } else if (certificationNumberState.length == 4 && checkSMSLiveData?.isSuccessful == false) {
                        "인증번호가 올바르지 않아요!"
                    } else {
                        if (currentCertificationNumberTimeLiveData != null)
                            convertMillisecondsToFormattedTime(currentCertificationNumberTimeLiveData!!).run { "${minutes}분 ${seconds}초 남음" }
                        else
                            "null"
                    }
                }
            }

            InputCertificationNumberScreen(
                certificationNumberValue = certificationNumberState,
                certificationNumberOnValueChange = {
                    certificationNumberState = it
                    if (certificationNumberState.length == 4) {
                        sendSMSLiveData?.body()?.data?.phoneConfirmId?.let {phoneConfirmId ->
                            viewModel.checkSMS(phoneConfirmId, certificationNumberState)
                        }
                    }
                },
                infoTextValue = underText,
                onResendVerificationCodeClick = {
                    viewModel.sendSMS(phoneNumber = phoneNumberState)
                    viewModel.cancelCertificationNumberCountDownTimer()
                    viewModel.startCertificationNumberTimer()
                }
            )
        }


        composable(route = "CreateNickname") { backStackEntry ->
            CreateNicknameScreen(viewModel, phoneNumberState)
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