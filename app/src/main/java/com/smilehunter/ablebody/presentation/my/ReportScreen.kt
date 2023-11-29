package com.smilehunter.ablebody.presentation.my

import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.smilehunter.ablebody.BuildConfig
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.utils.AbleBodyAlertDialog
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.utils.nonReplyClickable
import com.smilehunter.ablebody.utils.redirectToURL

@Composable
fun ReportRoute(
    onBackRequest: () -> Unit
) {
    ReportScreen(onBackRequest = onBackRequest)
    Log.d("ReportRoute", "ReportRoute")
}

@Preview
@Composable
fun ReportRoutePreview() {
    ReportRoute({})
}
@Composable
fun ReportScreen(
    onBackRequest: () -> Unit
) {
    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
            Text(
                text = "신고하기",
                modifier = Modifier.padding(horizontal = 50.dp, vertical = 15.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                )
            )},
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ){
            Spacer(modifier = Modifier
                .size(7.dp)
                .background(PlaneGrey)
            )
//            Text(text = "신고하는 이유를 선택해주세요")
            val text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = AbleBlue, fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)))) {
                    append("신고하는 이유")
                }
                append("를 선택해주세요")
            }

            Text(
                text = text,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                    fontWeight = FontWeight(500),
                    textAlign = TextAlign.Right,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                modifier = Modifier
                    .padding(20.dp)
                    .background(color = Color.White)
            )
            SettingList(listText = "불법적인 게시물이에요")
            SettingList(listText = "욕설을 해요")
            SettingList("음란성 글이에요")
            SettingList(listText = "차별/혐오 표현을 사용해요")
            SettingList(listText = "잘못된 정보를 제공하는 글이에요")
            SettingList(listText = "불쾌감을 줄 수 있는 사진이에요")
            SettingList(listText = "중복/도배성 게시물이에요")
            SettingList(listText = "기타 ")
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PlaneGrey)
            )
        }
    }
}

//@Composable
//fun ReportCompletePopup(
//    onBackRequest: () -> Unit,
//    onDismiss: () -> Unit
//) {
//    AbleBodyAlertDialog(
//        onDismissRequest = { onDismiss() },
//        positiveText = "확인",
//        positiveButtonOnClick = { onBackRequest() },
//        negativeButtonOnClick = {},
//    ) {
//        androidx.compose.material.Text(
//            text = "신고를 완료했어요.",
//            style = TextStyle(
//                fontSize = 18.sp,
//                lineHeight = 26.sp,
//                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
//                fontWeight = FontWeight(700),
//                color = AbleDark,
//                platformStyle = PlatformTextStyle(includeFontPadding = false)
//            )
//        )
//        androidx.compose.material.Text(
//            text = "애블바디팀이 검수 후 알려드릴게요.",
//            style = TextStyle(
//                fontSize = 14.sp,
//                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
//                fontWeight = FontWeight(400),
//                color = AbleDark,
//                platformStyle = PlatformTextStyle(includeFontPadding = false)
//            ),
//            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
//        )
//    }
//}

@Preview
@Composable
fun ReportScreenPreview() {
    ReportScreen(
        onBackRequest = {}
    )
}

@Preview
@Composable
fun ReportCompletePopupPreview() {
    ReportCompletePopup({},{})
}