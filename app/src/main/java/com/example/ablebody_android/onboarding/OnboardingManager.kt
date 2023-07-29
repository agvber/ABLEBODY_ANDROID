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
import com.example.ablebody_android.Gender
import com.example.ablebody_android.onboarding.data.CertificationNumberInfoMessage
import com.example.ablebody_android.onboarding.data.NicknameRule
import com.example.ablebody_android.onboarding.data.ProfileImages
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
    var nicknameTextState by remember { mutableStateOf("") }
    var genderState by remember { mutableStateOf<Gender?>(null) }
    var profileImageState by remember { mutableStateOf<ProfileImages?>(null) }

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

            val certificationNumberInfoMessage: CertificationNumberInfoMessage by remember {
                derivedStateOf {
                    if (currentCertificationNumberTimeLiveData == 0L) {
                        CertificationNumberInfoMessage(
                            message = "인증번호가 만료됐어요 다시 전송해주세요.",
                            isPositive = false
                        )
                    } else if (certificationNumberState.length == 4 && checkSMSLiveData?.isSuccessful == false) {
                        CertificationNumberInfoMessage(
                            message = "인증번호가 올바르지 않아요!",
                            isPositive = false
                        )
                    } else {
                        val result = currentCertificationNumberTimeLiveData?.let { time ->
                            convertMillisecondsToFormattedTime(time).run { "${minutes}분 ${seconds}초 남음" }
                        }
                        CertificationNumberInfoMessage(
                            message = result.toString(),
                            isPositive = true
                        )
                    }
                }
            }
            // TODO: LaunchedEffect 말고 더 좋은 방법 없나? ㅎ 
            LaunchedEffect(checkSMSLiveData) {
                if (checkSMSLiveData?.isSuccessful == true) {
                    Log.d("TEST", "")
                    viewModel.cancelCertificationNumberCountDownTimer()
                    navController.navigate("CreateNickname")
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
                infoMessage = certificationNumberInfoMessage,
                onResendVerificationCodeClick = {
                    viewModel.sendSMS(phoneNumber = phoneNumberState)
                    viewModel.cancelCertificationNumberCountDownTimer()
                    viewModel.startCertificationNumberTimer()
                }
            )
        }
        composable(route = "CreateNickname") {
            val availableNicknameCheckLiveData by viewModel
                .availableNicknameCheckLiveData.observeAsState(NicknameRule.Nothing)
            CreateNicknameScreen(
                nicknameText = nicknameTextState,
                nicknameTextChange = {
                    nicknameTextState = it
                    viewModel.checkAvailableNickname(nicknameTextState)
                                     },
                nicknameRuleState = availableNicknameCheckLiveData,
                phoneNumber = phoneNumberState,
                bottomButtonOnClick = { navController.navigate("InputGender") }
            )
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