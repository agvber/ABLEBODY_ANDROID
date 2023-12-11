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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.smilehunter.ablebody.presentation.my.SettingList
import com.smilehunter.ablebody.presentation.my.myInfo.MyInfoViewModel
import com.smilehunter.ablebody.presentation.my.myprofile.MyProfileViewModel
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleDeep
import com.smilehunter.ablebody.ui.theme.AbleRed
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.utils.AbleBodyAlertDialog
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.utils.nonReplyClickable

@Composable
fun MyInfoScreenRoute(
    myInfoViewModel: MyInfoViewModel = hiltViewModel(),
    onBackRequest: () -> Unit,
    withDrawOnClick: () -> Unit,
    editButtonOnClick: () -> Unit
) {
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
}

@Composable
fun WithdrawScreenRoute(
    myInfoViewModel: MyInfoViewModel = hiltViewModel(),
    onBackRequest: () -> Unit,
    drawReason: String,
    withDrawButtonOnClick: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        myInfoViewModel.getMyInfoData()
    }
    val userInfoData by myInfoViewModel.userLiveData.observeAsState()

    WithdrawScreen(
        nickname = userInfoData?.nickname ?: "",
        onBackRequest = onBackRequest,
        withDrawButtonOnClick = {
            myInfoViewModel.resignUser(drawReason)
            withDrawButtonOnClick()
        }
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
            ){
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
        ){
            SettingList(listText = "휴대폰 번호(아이디)", editText = phoneNumber)
            SettingList(listText = "닉네임", editText = nickname)
            SettingList(listText = "성별", editText = gender)
            SettingList(listText = "회원코드", editText = "#$uid")
            Spacer(modifier = Modifier.size(7.dp))
            SettingList(listText = "탈퇴하기", textColor = Color.Red, withDrawOnClick = withDrawOnClick)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyInfoEditTextField(
    editCategory: String,
    contentText: String,
) {
    var inputText by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 12.dp)
    ) {
        Text(
            text = editCategory,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            style = TextStyle(
                color = AbleDeep
            )
        )
        TextField(
            value = contentText,
            onValueChange = {
                inputText = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = PlaneGrey,
                disabledIndicatorColor = Color.Transparent // 비활성화된 상태의 밑줄 색상을 투명으로 설정
            ),
            enabled = !(editCategory == "성별" || editCategory == "회원코드")
        )
    }
}
@Composable
fun WithdrawBeforeScreen(
    onBackRequest: () -> Unit,
    withDrawReasonOnClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)},
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
            BackButtonTopBarLayout(onBackRequest = onBackRequest)},
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
//                            withDrawButtonOnClick()
                        },
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                            .height(55.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = AbleRed)

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
fun EditSavePopup(
    onBackRequest: () -> Unit,
    onDismiss: () -> Unit
) {
    AbleBodyAlertDialog(
        onDismissRequest = { onDismiss() },
        positiveText = "예",
        positiveButtonOnClick = { onBackRequest() },
        negativeText = "아니오",
        negativeButtonOnClick = { onDismiss() },
    ) {
        androidx.compose.material.Text(
            text = "저장할까요?",
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 26.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(700),
                color = AbleDark,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        androidx.compose.material.Text(
            text = "바꾼 정보를 저장합니다.",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                fontWeight = FontWeight(400),
                color = AbleDark,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
        )
    }
}



@Composable
fun WithDrawCompleteScreen() {
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier
            .padding(top = 14.dp, end = 16.dp)
            .nonReplyClickable {
                
            }
    ){
        Text(text = "홈으로")
    }
    Box(
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "회원 탈퇴 완료",
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
    MyInfoScreen({}, {}, {},"01012345678", "nickname", "남자", "000000")
}

@Preview(showSystemUi = true)
@Composable
fun WithdrawBeforeScreenPreview() {
    WithdrawBeforeScreen({},{})
}

@Preview(showSystemUi = true)
@Composable
fun WithdrawScreenPreview() {
    WithdrawScreen("nickname", {}, {})
}

@Preview(showSystemUi = true)
@Composable
fun WithDrawCompletePreview() {
    WithDrawCompleteScreen()
}