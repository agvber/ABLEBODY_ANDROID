package com.smilehunter.ablebody.presentation.main.ui.error_handling

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.SmallTextGrey
import com.smilehunter.ablebody.ui.utils.CustomButton
import com.smilehunter.ablebody.ui.utils.HighlightText

@Composable
fun InternalServerError(
    appRestartRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 130.dp)
    ) {
        HighlightText(
            string = "앗, 이미 삭제된\n컨텐츠예요!\n다른 컨텐츠는 어떠세요?",
            applyStringList = listOf("다른 컨텐츠"),
            applySpanStyle = SpanStyle(color = AbleBlue),
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(700),
                color = SmallTextGrey,
            )
        )
        CustomButton(
            text = "애블바디 다시 실행하기",
            onClick = appRestartRequest
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun InternalServerErrorPreview() {
    ABLEBODY_AndroidTheme {
        InternalServerError(appRestartRequest = {})
    }
}