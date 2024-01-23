package com.smilehunter.ablebody.presentation.main.ui.error_handler

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.utils.AbleBodyAlertDialog

@Composable
fun ForceAppVersionUpdateDialog(
    onDismissRequest: () -> Unit,
    positiveButtonOnClick: () -> Unit,
    negativeButtonOnClick: () -> Unit,
) {
    AbleBodyAlertDialog(
        onDismissRequest = onDismissRequest,
        positiveText = "업데이트",
        positiveButtonOnClick = positiveButtonOnClick,
        negativeText = "취소",
        negativeButtonOnClick = negativeButtonOnClick,
    ) {
        Text(
            text = "새로운 버전이 업데이트 됐어요!",
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 26.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(700),
                color = AbleDark,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        Text(
            text = "애블바디 유저분들의 의견을 반영하여 앱을\n개선했어요. 앱을 업데이트 해주세요 😀",
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

@Preview(showSystemUi = true)
@Composable
fun ForceAppVersionUpdateDialogPreview() {
    ABLEBODY_AndroidTheme {
        ForceAppVersionUpdateDialog(
            onDismissRequest = {  },
            positiveButtonOnClick = {  }) {
            
        }
    }
}