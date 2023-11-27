package com.smilehunter.ablebody.presentation.my

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.smilehunter.ablebody.BuildConfig
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.AbleDeep
import com.smilehunter.ablebody.ui.theme.AbleLight
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.AbleBodyAlertDialog
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.utils.nonReplyClickable
import com.smilehunter.ablebody.utils.redirectToURL


@Composable
fun SettingScreen(
    onBackRequest: () -> Unit,
    suggestonClick: () -> Unit,
    myInfoOnClick: () -> Unit,
    alarmOnClick: () -> Unit
) {
    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
            Text(
                text = "설정",
                modifier = Modifier.padding(horizontal = 70.dp, vertical = 15.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                )
            )},
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(PlaneGrey)
        ){
            SuggestList(suggestonClick = suggestonClick)
            Spacer(modifier = Modifier.size(7.dp))
            SettingList(listText = "내 정보", myInfoOnClick = myInfoOnClick)
            Spacer(modifier = Modifier.size(7.dp))
            SettingList(listText = "알림", alarmOnClick = alarmOnClick)
            Spacer(modifier = Modifier.size(7.dp))
            SettingList("1:1 문의하기", linkUrl = "kakaotalk channel")
            Spacer(modifier = Modifier.size(7.dp))
            SettingList(listText = "서비스 이용 약관", linkUrl = "service agreement")
            SettingList(listText = "개인정보 수집 및 이용", linkUrl = "privacy policy")
            SettingList(listText = "개인정보 제3자 제공", linkUrl = "thirdparty sharing consent")
            SettingList(listText = "개인정보처리방침", linkUrl = "Personal Information Processing Policy")
            SettingList(listText = "앱 버전")
            Spacer(modifier = Modifier.size(7.dp))
            SettingList("로그아웃", textColor = Color.Red)
        }
    }
}

@Composable
fun AlarmRoute(
    viewModel: MyProfileViewModel = hiltViewModel(),
    onBackRequest: () -> Unit
) {
    val getAlarmAgreeData by viewModel.getUserAdConsentLiveData.observeAsState(false)
    Log.d("받아온 알림 데이터", getAlarmAgreeData.toString())


    LaunchedEffect(key1 = true) {
        viewModel.getData()
    }

    AlarmPage(
        onBackRequest = onBackRequest,
        getAlarmAgree = getAlarmAgreeData,
        passAlarmAgree =  {viewModel.changeUserAdConsent(it)}
    )
}

@Composable
fun SuggestRoute(
    viewModel: MyProfileViewModel = hiltViewModel(),
    onBackRequest: () -> Unit
) {
    SuggestPage(onBackRequest = onBackRequest, suggestText = {viewModel.sendSuggest(it)})
}

@Composable
fun SuggestList(
    suggestonClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(Color.White)
            .nonReplyClickable {
                suggestonClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painter = painterResource(id = R.drawable.ablebody_notification_logo),
            contentDescription = "profile",
            colorFilter = ColorFilter.tint(AbleBlue),
            modifier = Modifier.padding(start = 20.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        val text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = AbleBlue, fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)))) {
                append("애블바디")
            }
            append("에게 제안해주세요")
        }

        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                fontWeight = FontWeight(500),
                textAlign = TextAlign.Right,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.airplane),
            contentDescription = "sueggestIcon",
            modifier = Modifier.padding(end = 10.dp)
        )
    }
}

@Composable
fun SettingList(
    listText: String,
    linkUrl: String = "",
    textColor: Color = Color.Black,
    editText: String = "",
    myInfoOnClick: () -> Unit = {},
    alarmOnClick: () -> Unit = {},
    withDrawOnClick: () -> Unit = {},
    withDrawReasonOnClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val manager = context.packageManager
    val info = manager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
    Log.d("PackageName = ", "PackageName = ${info.packageName} VersionCode = ${info.versionCode} VersionName = ${info.versionName}")

    Log.d("앱버전",BuildConfig.VERSION_NAME)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(Color.White)
            .nonReplyClickable(onClick = {
//                else if (listText == "쓰지 않는 앱이에요.") {
//                withDrawReasonOnClick("쓰지 않는 앱이에요.")
//            } else if (listText == "볼만한 컨텐츠가 없어요.") {
//                withDrawReasonOnClick("볼만한 컨텐츠가 없어요.")
//            } else if (listText == "앱에 오류가 있어요.") {
//                withDrawReasonOnClick("앱에 오류가 있어요.")
//            } else if (listText == "앱을 어떻게 쓰는지 모르겠어요.") {
//                withDrawReasonOnClick("앱을 어떻게 쓰는지 모르겠어요.")
//            } else if (listText == "기타") {
//                withDrawReasonOnClick("기타")
//            }
                if (listText == "로그아웃") {
                    showDialog = true
                } else if (listText == "내 정보") {
                    myInfoOnClick()
                } else if (listText == "알림") {
                    alarmOnClick()
                } else if (listText == "쓰지 않는 앱이에요.") {
                    withDrawReasonOnClick()
                } else if (listText == "볼만한 컨텐츠가 없어요.") {
                    withDrawReasonOnClick()
                } else if (listText == "앱에 오류가 있어요.") {
                    withDrawReasonOnClick()
                } else if (listText == "앱을 어떻게 쓰는지 모르겠어요.") {
                    withDrawReasonOnClick()
                } else if (listText == "기타") {
                    withDrawReasonOnClick()
                } else if (listText == "탈퇴하기") {
                    withDrawOnClick()
                } else {
                    redirectToURL(context, linkUrl)
                }
            }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = listText,
            color = textColor,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                fontWeight = FontWeight(500),
                textAlign = TextAlign.Right,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            modifier = Modifier
                .padding(horizontal = 25.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        if(editText.isNotEmpty()){
            Text(
                text = editText,
                color = textColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                    fontWeight = FontWeight(500),
                    textAlign = TextAlign.Right,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                modifier = Modifier
                    .padding(horizontal = 25.dp)
            )
        }else {
            Icon(
                Icons.Filled.KeyboardArrowRight,
                contentDescription = linkUrl,
                Modifier
                    .padding(10.dp)
                    .size(24.dp),
                tint = Color.Gray
            )
        }
    }
    if (showDialog) {
        LogoutAlertDialog( {showDialog = false}  )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestPage(
    onBackRequest: () -> Unit,
    suggestText: (String) -> Unit
) {
    val version = BuildConfig.VERSION_NAME
    Log.d("version", version)

    var showDialog by remember { mutableStateOf(false) }
    var showExitWarningDialog by remember { mutableStateOf(false) }

    var inputText by remember {
        mutableStateOf("")
    }

    BackHandler(
        onBack = {
            // 뒤로가기 버튼을 누를 때 ExitWarningPopup 표시
            if (showExitWarningDialog || showDialog || inputText.isNotBlank()) {
                showExitWarningDialog = true
            } else {
                onBackRequest()
            }
        }
    )

    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = {
                // 뒤로가기 버튼을 누를 때 ExitWarningPopup 표시
                if (showExitWarningDialog || showDialog || inputText.isNotBlank()) {
                    showExitWarningDialog = true
                } else {
                    onBackRequest()
                }
            })
            Text(
                text = "애블바디에게 제안하기",
                modifier = Modifier.padding(horizontal = 70.dp, vertical = 15.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 글자 수 제한
            val maxCharCount = 300
            val isButtonEnabled = inputText.isNotBlank() && inputText.length <= maxCharCount

            val text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = AbleBlue, fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)))) {
                    append("애블바디")
                }
                append("에게 제안해요!")
            }

            Text(
                text = text,
                style = TextStyle(
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                    fontWeight = FontWeight(500),
                    textAlign = TextAlign.Right,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                modifier = Modifier.padding(20.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(20.dp)
                    .background(PlaneGrey, shape = RoundedCornerShape(16.dp))
            ) {
                TextField(
                    value = inputText,
                    onValueChange = {
                        // 글자 수 제한
                        if (it.length <= maxCharCount) {
                            inputText = it
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    placeholder = { Text(text = "애블바디에 이런 기능이 있었으면 좋겠어요.") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = PlaneGrey,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            Text(
                text = "글자수 제한 (${inputText.length}/$maxCharCount)", // 글자 수 표시 부분
                color = AbleDeep,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                    fontWeight = FontWeight(500),
                    textAlign = TextAlign.Right,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp)
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                androidx.compose.material3.Button(
                    onClick = {
                        suggestText(inputText)
                        showDialog = true
                    },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .height(55.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = AbleBlue),
                    enabled = isButtonEnabled // 버튼 활성화 여부를 설정
                ) {
                    Text(
                        text = "애블바디팀에게 보내기",
                        color = Color.White
                    )
                }
            }
        }
        if (showDialog) {
            SuggestCompletePopup( onBackRequest = onBackRequest, onDismiss = { showDialog = false })
        }
        if (showExitWarningDialog) {
            ExitWarningPopup(
                onBackRequest = onBackRequest,
                onDismiss = { showExitWarningDialog = false },
                onConfirm = {
                    showExitWarningDialog = false
                    onBackRequest()
                }
            )
        }
    }
}

@Composable
fun AlarmPage(
    onBackRequest: () -> Unit,
    getAlarmAgree: Boolean,
    passAlarmAgree: (Boolean) -> Unit,
) {
    var alarmAgreeStatus = getAlarmAgree
    Log.d("보여주는 알림 데이터", alarmAgreeStatus.toString()) //true <-> false

    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
            Text(
                text = "알림",
                modifier = Modifier.padding(horizontal = 70.dp, vertical = 15.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "혜택 · 마케팅 알림",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(500),
                        color = if (alarmAgreeStatus) Color.Black else SmallTextGrey,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier.weight(1f)
                )
                MarketingAlarmToggleButton(buttonState = alarmAgreeStatus) { toggledState ->
                    Log.d("toggledState", toggledState.toString())
                    alarmAgreeStatus = toggledState
//                    Log.d("보낼 거", "동의 여부: $toggledState")
                    passAlarmAgree(toggledState)
                }
            }
            BenefitDescription(alarmAgree = alarmAgreeStatus)
        }
    }
}

@Composable
fun LogoutAlertDialog(
    onDismiss: () -> Unit
) {
    AbleBodyAlertDialog(
        onDismissRequest = { onDismiss() },
        positiveText = "아니오",
        positiveButtonOnClick = { onDismiss() },
        negativeText = "예",
        negativeButtonOnClick = { /*TODO : 로그아웃*/ },
    ) {
        androidx.compose.material.Text(
            text = "로그아웃",
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
            text = "로그아웃 할까요?",
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
fun MarketingAlarmToggleButton(
    buttonState: Boolean,
    alaramButtononToggle: (Boolean) -> Unit
) {
    var isToggle = buttonState

    Card(
        shape = RoundedCornerShape(20.dp), // 여기서 테두리를 둥글게 설정
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .clickable {
                    isToggle = !isToggle
                    alaramButtononToggle(isToggle)
                },
            contentAlignment = Alignment.Center
        ) {
            if (isToggle) {
                Row() {
                    Icon(
                        painter = painterResource(id = R.drawable.alarm_able_toggle_btn),
                        contentDescription = null,
                        tint = AbleBlue
                    )
                }
            } else {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.alarm_enable_toggle_btn),
                        contentDescription = null,
                        tint = AbleLight
                    )
                }
            }
        }
    }
}

@Composable
fun BenefitDescription(
    alarmAgree: Boolean
) {
    val descriptionText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)))) {
            append("혜택 알림 수신에 동의")
        }
        append("하셨어요!\n애블바디에서 진행하는 여러가지 이벤트에 대해\n가장 먼저 알려드릴게요 \uD83D\uDE00")
    }

    val additionalText = buildAnnotatedString {
        append("알림 수신에 동의하시면, 앞으로 ")
        withStyle(style = SpanStyle(fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)))) {
            append("새롭게 추가될\n기능들을 먼저 이용")
        }
        append("해 보실 수 있어요!")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = if (alarmAgree) 8.dp else 16.dp,
                bottom = if (alarmAgree) 8.dp else 16.dp
            )
            .background(PlaneGrey, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = if (alarmAgree) {
                Modifier.padding(start = 20.dp, end = 20.dp, bottom = 8.dp)
            } else {
                Modifier.padding(start = 20.dp)
            }
        ) {
            Text(text = if (alarmAgree) descriptionText else additionalText)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    SettingScreen({},{},{},{})
}
@Preview(showBackground = true)
@Composable
fun SuggestPagePreview() {
    SuggestPage({}, {})
}

@Preview(showBackground = true)
@Composable
fun LogoutAlertDialogPreview() {
    LogoutAlertDialog({})
}

@Composable
fun SuggestCompletePopup(
    onBackRequest: () -> Unit,
    onDismiss: () -> Unit
) {
    AbleBodyAlertDialog(
        onDismissRequest = { onDismiss() },
        positiveText = "확인",
        positiveButtonOnClick = { onBackRequest() },
        negativeButtonOnClick = {},
    ) {
        androidx.compose.material.Text(
            text = "정말 고마워요",
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
            text = "애블바디팀이 적극적으로 고려해볼게요.",
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
@Preview(showBackground = true)
@Composable
fun SuggestCompletePopupPreview() {
    SuggestCompletePopup({},{})
}


@Composable
fun ExitWarningPopup(
    onBackRequest: () -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AbleBodyAlertDialog(
        onDismissRequest = { onDismiss() },
        positiveText = "예",
        positiveButtonOnClick = { onBackRequest() },
        negativeText = "아니오",
        negativeButtonOnClick = { onDismiss() },
    ) {
        androidx.compose.material.Text(
            text = "정말 나가시겠어요?",
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
            text = "지금 나가면 작성 중인 내용이 모두 사라져요.",
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
@Preview(showBackground = true)
@Composable
fun ExitWarningPopupPreview() {
    ExitWarningPopup({}, {}, {})
}

@Preview(showSystemUi = true)
@Composable
fun AlarmPagePreview() {
    Column(){
//        AlarmPage({}, true)
        AlarmPage({}, true, {})
    }
}
