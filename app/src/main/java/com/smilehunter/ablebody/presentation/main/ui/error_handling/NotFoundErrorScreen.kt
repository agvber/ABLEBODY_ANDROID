package com.smilehunter.ablebody.presentation.main.ui.error_handling

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
fun NotFoundErrorScreen(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ErrorScreen(
        onClick = onClick,
        modifier = modifier
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
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun NotFoundErrorScreenPreview() {
    ABLEBODY_AndroidTheme {
        NotFoundErrorScreen(onClick = {})
    }
}