package com.smilehunter.ablebody.presentation.my

import android.content.Intent
import android.net.Uri
import android.widget.ToggleButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.smilehunter.ablebody.ui.utils.previewPlaceHolder
import com.smilehunter.ablebody.utils.redirectToURL

@Composable
fun SettingScreen(
    onBackRequest: () -> Unit
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
            SuggestList()
            Spacer(modifier = Modifier.size(7.dp))
            SettingList("내 정보","")
            Spacer(modifier = Modifier.size(7.dp))
            SettingList("알림","")
            Spacer(modifier = Modifier.size(7.dp))
            SettingList("1:1 문의하기","")
            Spacer(modifier = Modifier.size(7.dp))
            SettingList("서비스 이용 약관","service agreement")
            SettingList("개인정보 수집 및 이용","privacy policy")
            SettingList("개인정보 제3자 제공","thirdparty sharing consent")
            SettingList("개인정보처리방침","Personal Information Processing Policy")
            SettingList("앱 버전","")
            Spacer(modifier = Modifier.size(7.dp))
            SettingList("로그아웃","", Color.Red)
        }
    }
}

@Composable
fun SuggestList() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(Color.White),
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
//        Image(
//            painter = painterResource(id = R.drawable.airplane),
//            contentDescription = "sueggestIcon"
//        )
    }
}

@Composable
fun SettingList(
    listText: String,
    linkUrl: String,
    textColor: Color = Color.Black
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(Color.White),
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
            modifier = Modifier.padding(horizontal = 25.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            Icons.Filled.KeyboardArrowRight,
            contentDescription = linkUrl,
            Modifier
                .clickable(onClick = {
//                    OpenLink(linkUrl)
                    redirectToURL(context, linkUrl)
                })
                .padding(10.dp)
                .size(24.dp),
            tint = Color.Gray
        )
    }
}

//@Composable
//fun OpenLink(linkUrl: String) {
//    val context = LocalContext.current
//    val intent = Intent(Intent.ACTION_VIEW).apply {
//        data = Uri.parse(linkUrl)
//    }
//    context.startActivity(intent)
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestPage(
    onBackRequest: () -> Unit
) {
    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
            Text(
                text = "애블바디에게 제안하기",
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
        ){
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
            ){
                var inputText by remember {
                    mutableStateOf("")
                }

                TextField(
                    value = inputText,
                    onValueChange = {
                        inputText = it
                    },
                    modifier = Modifier.fillMaxSize(),
                    placeholder = { Text( text = "애블바디에 이런 기능이 있었으면 좋겠어요.")},
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = PlaneGrey,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
//                TextField(
//                    value = textValue,
//                    onValueChange = { textValue = it },
//                    placeholder = {
//                        Text("여기에 힌트 텍스트를 입력하세요.")
//                    }
//                )
            }
            Text(
                text = "글자수 제한 (0/300)",
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
            ){
                androidx.compose.material3.Button(
                    onClick = {

                    },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .height(55.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = AbleBlue),

                    ) {
                    Text(
                        text = "애블바디팀에게 보내기",
                        color = Color.White
                    )
                }

            }
        }
    }
}

@Composable
fun AlarmPage(
    onBackRequest: () -> Unit
) {
    var checked by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
            Text(
                text = "알림",
                modifier = Modifier.padding(horizontal = 70.dp, vertical = 15.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                )
            )},
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "혜택·마케팅 알림",
                    style = TextStyle(
                        fontSize = 19.sp,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                        fontWeight = FontWeight(500),
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        uncheckedThumbColor = Color.White,
                        checkedTrackColor = AbleBlue,
                        uncheckedTrackColor = AbleLight
                    ),
//                    modifier = Modifier.scale(1.1f)  // 여기서 크기를 조절합니다.
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(horizontal = 10.dp)
                    .background(PlaneGrey, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ){
                val text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)))) {
                        append("혜택 알림 수신에 동의")
                    }
                    append("하셨어요!\n" +
                            "애블바디에서 진행하는 여러가지 이벤트에 대해\n가장 먼저 알려드릴게요\uD83D\uDE00")
                }
                Text(text = text)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    SettingScreen({})
}

@Preview(showBackground = true)
@Composable
fun SuggestPagePreview() {
    SuggestPage({})
}

@Preview(showBackground = true)
@Composable
fun LogoutAlertDialogPreview() {
    ABLEBODY_AndroidTheme {
        AbleBodyAlertDialog(
            onDismissRequest = {},
            positiveText = "아니오",
            positiveButtonOnClick = {},
            negativeText = "예",
            negativeButtonOnClick = {},
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
}

@Preview(showBackground = true)
@Composable
fun ExitAlertDialogPreview() {
    ABLEBODY_AndroidTheme {
        AbleBodyAlertDialog(
            onDismissRequest = {},
            positiveText = "확인",
            positiveButtonOnClick = {},
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
}

@Preview(showBackground = true)
@Composable
fun SuggestAlertDialogPreview() {
    ABLEBODY_AndroidTheme {
        AbleBodyAlertDialog(
            onDismissRequest = {},
            positiveText = "예",
            positiveButtonOnClick = {},
            negativeText = "아니오",
            negativeButtonOnClick = {},
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
}

@Preview(showBackground = true)
@Composable
fun AlarmPagePreview() {
    AlarmPage({})
}
