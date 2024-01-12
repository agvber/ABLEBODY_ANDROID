package com.smilehunter.ablebody.presentation.my.report.ui

import android.content.pm.PackageManager
import android.widget.Toast
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
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.smilehunter.ablebody.BuildConfig
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.data.dto.request.ReportRequest
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.my.report.ReportViewModel
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.PlaneGrey
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.AbleBodyAlertDialog
import com.smilehunter.ablebody.ui.utils.BackButtonTopBarLayout
import com.smilehunter.ablebody.ui.utils.SimpleErrorHandler
import com.smilehunter.ablebody.utils.nonReplyClickable

@Composable
fun ReportRoute(
    reportViewModel: ReportViewModel = hiltViewModel(),
    onErrorOccur: (ErrorHandlerCode) -> Unit,
    onBackRequest: () -> Unit,
    uid: String
) {
    var reportCompleteDialog by remember { mutableStateOf(false) }
    val errorData by reportViewModel.sendErrorLiveData.observeAsState()

    ReportScreen(
        onBackRequest = onBackRequest,
        onReportCompleteDialog = { reportCompleteDialog = true },
        uid = uid
    )

    if (errorData != null) {
        val context = LocalContext.current
        Toast.makeText(context, "네트워크 에러가 발생했습니다\n다시 시도해주세요", Toast.LENGTH_LONG).show()
    } else if (reportCompleteDialog) {
        ReportCompletePopup(onBackRequest = onBackRequest, { reportCompleteDialog = false })
    }
}

@Composable
fun ReportScreen(
    reportViewModel: ReportViewModel = hiltViewModel(),
    onBackRequest: () -> Unit,
    onReportCompleteDialog: () -> Unit,
    uid: String
) {
    val reportOptions = listOf(
        "불법적인 게시물이에요",
        "욕설을 해요",
        "음란성 글이에요",
        "차별/혐오 표현을 사용해요",
        "잘못된 정보를 제공하는 글이에요",
        "불쾌감을 줄 수 있는 사진이에요",
        "중복/도배성 게시물이에요",
        "기타 " //설정 화면에서 "기타"랑 중복돼서 "기타 "로 변경
    )

    Scaffold(
        topBar = {
            BackButtonTopBarLayout(onBackRequest = onBackRequest)
            Text(
                text = "신고하기",
                modifier = Modifier.padding(horizontal = 50.dp, vertical = 15.dp),
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
                .background(Color.White)
        ) {
            Spacer(
                modifier = Modifier
                    .height(7.dp)
                    .fillMaxWidth()
                    .background(PlaneGrey)
            )
            val text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = AbleBlue,
                        fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold))
                    )
                ) {
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

            reportOptions.forEach { option ->
                SettingReportList(
                    listText = option,
                    onReportOnClick = {
                        reportViewModel.reportUser(
                            ReportRequest(
                                ReportRequest.ContentType.User,
                                uid.toLong(),
                                option,
                                ""
                            )
                        )
                    },
                    onReportCompleteDialog = onReportCompleteDialog
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PlaneGrey)
            )
        }
    }
}


@Composable
fun SettingReportList(
    listText: String,
    linkUrl: String = "",
    textColor: Color = Color.Black,
    editText: String = "",
    withDrawOnClick: () -> Unit = {},
    onReportCompleteDialog: () -> Unit = {},
    onReportOnClick: () -> Unit
) {
    val context = LocalContext.current
    val manager = context.packageManager
    val info = manager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(Color.White)
            .nonReplyClickable(onClick = {
                when (listText) {
                    "탈퇴하기" -> withDrawOnClick()
                    "불법적인 게시물이에요",
                    "욕설을 해요",
                    "음란성 글이에요",
                    "차별/혐오 표현을 사용해요",
                    "잘못된 정보를 제공하는 글이에요",
                    "불쾌감을 줄 수 있는 사진이에요",
                    "중복/도배성 게시물이에요",
                    "기타 " -> {
                        onReportCompleteDialog()
                        onReportOnClick()
                    }

                    else -> { /* 기타 경우에 대한 처리 */
                    }
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
        if (editText.isNotEmpty()) {
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
        } else {
            if (listText == "앱 버전") {
                Text(
                    text = BuildConfig.VERSION_NAME,
                    color = SmallTextGrey,
                    modifier = Modifier.padding(end = 16.dp)
                )
            } else {
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
    }
}

@Composable
fun ReportCompletePopup(
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
            text = "신고를 완료했어요.",
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
            text = "애블바디팀이 검수 후 알려드릴게요.",
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

@Preview
@Composable
fun ReportRoutePreview() {
    ReportRoute(onErrorOccur = {}, onBackRequest = {}, uid = "")
}

@Preview
@Composable
fun ReportScreenPreview() {
    ReportScreen(
        onBackRequest = {},
        onReportCompleteDialog = {},
        uid = ""
    )
}

@Preview
@Composable
fun ReportCompletePopupPreview() {
    ReportCompletePopup({}, {})
}