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
fun NetworkConnectionErrorDialog(
    onDismissRequest: () -> Unit,
    positiveButtonOnClick: () -> Unit,
    negativeButtonOnClick: () -> Unit,
) {
    AbleBodyAlertDialog(
        onDismissRequest = onDismissRequest,
        positiveText = "재시도",
        positiveButtonOnClick = positiveButtonOnClick,
        negativeText = "설정",
        negativeButtonOnClick = negativeButtonOnClick,
    ) {
        Text(
            text = "네트워크 오류",
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
            text = "Wifi나 3G/LTE/5G를\n연결 후 재시도 해주세요 ⚡️",
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
fun NetworkConnectionErrorDialogPreview() {
    ABLEBODY_AndroidTheme {
        NetworkConnectionErrorDialog(
            onDismissRequest = {  },
            positiveButtonOnClick = {  }) {
            
        }
    }
}