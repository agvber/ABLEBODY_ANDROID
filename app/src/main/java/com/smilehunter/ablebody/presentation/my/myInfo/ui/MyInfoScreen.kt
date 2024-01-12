package com.smilehunter.ablebody.presentation.my.myInfo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.my.setting.ui.SettingList
import com.smilehunter.ablebody.presentation.my.myInfo.MyInfoViewModel
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleRed
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.SimpleErrorHandler
import com.smilehunter.ablebody.utils.nonReplyClickable

@Composable
fun MyInfoScreenRoute(
    myInfoViewModel: MyInfoViewModel = hiltViewModel(),
    onErrorOccur: (ErrorHandlerCode) -> Unit,
    onBackRequest: () -> Unit,
    withDrawOnClick: () -> Unit,
    editButtonOnClick: () -> Unit
) {
    val errorData by myInfoViewModel.sendErrorLiveData.observeAsState()

    LaunchedEffect(key1 = true) {
        myInfoViewModel.getMyInfoData()
    }
    val userInfoData by myInfoViewModel.userLiveData.observeAsState()

    MyInfoScreen(
        onBackRequest = onBackRequest,
        withDrawOnClick = withDrawOnClick,
        editButtonOnClick = editButtonOnClick,
        phoneNumber = userInfoData?.phoneNumber ?: "",
        nickname = userInfoData?.nickname ?: "",
        gender = when (userInfoData?.gender) {
            Gender.MALE -> "남자"
            Gender.FEMALE -> "여자"
            else -> ""
        },
        uid = userInfoData?.uid ?: ""
    )

    SimpleErrorHandler(
        refreshRequest = { myInfoViewModel.getMyInfoData() },
        onErrorOccur = onErrorOccur,
        isError = errorData != null,
        throwable = errorData
    )
}

@Composable
fun WithdrawScreenRoute(
    myInfoViewModel: MyInfoViewModel = hiltViewModel(),
    onErrorOccur: (ErrorHandlerCode) -> Unit,
    onBackRequest: () -> Unit,
    drawReason: String,
    withDrawButtonOnClick: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        myInfoViewModel.getMyInfoData()
    }
    val userInfoData by myInfoViewModel.userLiveData.observeAsState()
    val errorData by myInfoViewModel.sendErrorLiveData.observeAsState()

    WithdrawScreen(
        nickname = userInfoData?.nickname ?: "",
        onBackRequest = onBackRequest,
        withDrawButtonOnClick = {
            myInfoViewModel.resignUser(drawReason)
            withDrawButtonOnClick()
        }
    )
    SimpleErrorHandler(
        refreshRequest = { myInfoViewModel.getMyInfoData() },
        onErrorOccur = onErrorOccur,
        isError = errorData != null,
        throwable = errorData
    )
}

@Composable
fun MyInfoScreen(
    onBackRequest: () -> Unit,
    withDrawOnClick: () -> Unit,
    editButtonOnClick: () -> Unit,
    phoneNumber: String,
    nickname: String,
    gender: String,
    uid: String
) {
    Scaffold(
        topBar = {
            BackButtonTopBarLayout(
                onBackRequest = onBackRequest,
                titleText = "내 정보"
            ) {
                Text(
                    text = "수정",
                    modifier = Modifier.nonReplyClickable {
                        editButtonOnClick()
                    }
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(PlaneGrey)
        ) {
            SettingList(listText = "휴대폰 번호(아이디)", editText = phoneNumber)
            SettingList(listText = "닉네임", editText = nickname)
            SettingList(listText = "성별", editText = gender)
            SettingList(listText = "회원코드", editText = "#$uid")
            Spacer(modifier = Modifier.size(7.dp))
            SettingList(listText = "탈퇴하기", textColor = Color.Red, withDrawOnClick = withDrawOnClick)
        }
    }
}

@Composable
fun WithdrawBeforeScreen(
    onBackRequest: () -> Unit,
    withDrawReasonOnClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
            ) {
                Text(
                    text = "탈퇴하는 이유가 무엇인가요?",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                        fontWeight = FontWeight(500),
                        textAlign = TextAlign.Right,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                )
            }
            SettingList("쓰지 않는 앱이에요.", withDrawReasonOnClick = withDrawReasonOnClick)
            SettingList("볼만한 컨텐츠가 없어요.", withDrawReasonOnClick = withDrawReasonOnClick)
            SettingList("앱에 오류가 있어요.", withDrawReasonOnClick = withDrawReasonOnClick)
            SettingList("앱을 어떻게 쓰는지 모르겠어요.", withDrawReasonOnClick = withDrawReasonOnClick)
            SettingList("기타", withDrawReasonOnClick = withDrawReasonOnClick)
        }
    }
}

@Composable
fun WithdrawScreen(
    nickname: String,
    onBackRequest: () -> Unit,
    withDrawButtonOnClick: () -> Unit
) {
    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = AbleBlue)) {
                            append("$nickname")
                        }
                        append("님 아쉬워요!")
                    },
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                        fontWeight = FontWeight(500),
                        textAlign = TextAlign.Right,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                )

                Spacer(modifier = Modifier.size(12.dp))

                Text(text = "계정을 삭제하면 모든 활동 정보가 사라져요.")

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    androidx.compose.material3.Button(
                        onClick = {
                            withDrawButtonOnClick()
                        },
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                            .height(55.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = AbleRed
                        )

                    ) {
                        Text(
                            text = "탈퇴하기",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun WithDrawCompleteScreen(
    myInfoViewModer: MyInfoViewModel = hiltViewModel(),
    onErrorOccur: (ErrorHandlerCode) -> Unit
) {
    val errorData by myInfoViewModer.sendErrorLiveData.observeAsState()

    SimpleErrorHandler(
        refreshRequest = { myInfoViewModer.getMyInfoData() },
        onErrorOccur = onErrorOccur,
        isError = errorData != null,
        throwable = errorData
    )
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "홈으로",
            modifier = Modifier
                .nonReplyClickable {
                    myInfoViewModer.deleteToken()
                }
                .padding(top = 14.dp, end = 16.dp)
                .align(Alignment.TopEnd)
        )
        Text(
            text = "회원 탈퇴 완료",
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                color = AbleBlue,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(500),
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MyInfomationScreenPreview() {
    MyInfoScreen({}, {}, {}, "01012345678", "nickname", "남자", "000000")
}

@Preview(showSystemUi = true)
@Composable
fun WithdrawBeforeScreenPreview() {
    WithdrawBeforeScreen(onBackRequest = {}, withDrawReasonOnClick = {})
}

@Preview(showSystemUi = true)
@Composable
fun WithdrawScreenPreview() {
    WithdrawScreen("nickname", {}, {})
}

@Preview(showSystemUi = true)
@Composable
fun WithDrawCompletePreview() {
    WithDrawCompleteScreen(onErrorOccur = {})
}