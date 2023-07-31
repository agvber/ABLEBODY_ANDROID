package com.example.ablebody_android.onboarding

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ablebody_android.R
import com.example.ablebody_android.onboarding.utils.checkPermission
import com.example.ablebody_android.utils.CustomButton
import com.example.ablebody_android.utils.HighlightText
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.SmallTextGrey
import kotlinx.coroutines.launch


@Composable
private fun IntroContentLayout(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 130.dp)
    ) {
        HighlightText(
            string = "나만의 운동 스타일을 찾다\n애블바디",
            colorStringList = listOf("애블바디"),
            color = AbleBlue,
            style = TextStyle(
                fontSize = 27.sp,
                color = AbleDark,
                lineHeight = 40.sp,
                fontFamily = FontFamily(Font(resId = R.font.noto_sans_cjkr_black))
            ),
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
        )

        HighlightText(
            string = "운동에 진심인 사람들의\n다양한 운동 스타일을 만나보세요.",
            colorStringList = listOf("다양한 운동 스타일"),
            color = AbleBlue,
            style = TextStyle(
                fontSize = 17.sp,
                color = SmallTextGrey,
                lineHeight = 27.2.sp,
                fontFamily = FontFamily(Font(resId = R.font.noto_sans_cjkr_black))
            ),
            modifier = Modifier.padding(vertical = 28.dp, horizontal = 16.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun IntroContentLayoutPreview() {
    IntroContentLayout()
}


@Composable
private fun IntroBottomLayout(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomButton(
            text = "시작하기",
            onClick = onClick,
        )

        Row(
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(text = "이미 계정이 있나요? ")
            HighlightText(
                string = "로그인",
                colorStringList = listOf("로그인"),
                color = AbleBlue,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    /* TODO: (온보딩/시작하기) 로그인 버튼 클릭 후 이벤트 */
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IntroBottomLayoutPreview() {
    IntroBottomLayout() {  }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IntroScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}

    val coroutineScope = rememberCoroutineScope()

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    PermissionExplanationBottomSheet(
        sheetState = bottomSheetState,
        bottomButtonClick = {
            coroutineScope.launch { bottomSheetState.hide() }
            launcher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_MEDIA_IMAGES))
            navController.navigate("InputPhoneNumber")
        }
    ) {
        Scaffold(
            bottomBar = {
                IntroBottomLayout(
                    onClick = {
                        coroutineScope.launch {
                            if (
                                !checkPermission(context, Manifest.permission.POST_NOTIFICATIONS) ||
                                !checkPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
                            ) {
                                bottomSheetState.show()
                            } else {
                                navController.navigate("InputPhoneNumber")
                            }
                        }
                    }
                )
                        },
        ) {
            IntroContentLayout(modifier = Modifier.padding(it))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IntroScreenPreview() {
    IntroScreen(navController = rememberNavController())
}