package com.smilehunter.ablebody.presentation.main.ui.error_handler

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
import com.smilehunter.ablebody.ui.utils.HighlightText

@Composable
fun InternalServerError(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ErrorScreen(
        onClick = onClick,
        buttonText = "애블바디 다시 실행하기",
        modifier = modifier
    ) {
        HighlightText(
            string = "앗, 뭔가 문제가 있어요!\n애블바디 팀이 빠르게\n해결할게요.",
            applyStringList = listOf("애블바디 팀"),
            applySpanStyle = SpanStyle(color = AbleBlue),
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_bold)),
                fontWeight = FontWeight(700),
                color = SmallTextGrey,
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun InternalServerErrorPreview() {
    ABLEBODY_AndroidTheme {
        InternalServerError(onClick = {})
    }
}