package com.smilehunter.ablebody.presentation.my.suggest.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.my.setting.ui.SuggestPage
import com.smilehunter.ablebody.presentation.my.suggest.SuggestViewModel
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.utils.SimpleErrorHandler
import com.smilehunter.ablebody.utils.nonReplyClickable

@Composable
fun SuggestRoute(
    suggestViewModel: SuggestViewModel = hiltViewModel(),
    onBackRequest: () -> Unit
) {
    val errorData by suggestViewModel.sendErrorLiveData.observeAsState()
    SuggestPage(onBackRequest = onBackRequest, suggestText = { suggestViewModel.sendSuggest(it) })

    //다이얼로그 안 뜨게 하자!
    if (errorData != null) {
        val context = LocalContext.current
        Toast.makeText(context, "네트워크 에러가 발생했습니다\n다시 시도해주세요", Toast.LENGTH_LONG).show()
    }
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
    ) {
        Image(
            painter = painterResource(id = R.drawable.ablebody_notification_logo),
            contentDescription = "profile",
            colorFilter = ColorFilter.tint(AbleBlue),
            modifier = Modifier.padding(start = 20.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        val text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = AbleBlue,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold))
                )
            ) {
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