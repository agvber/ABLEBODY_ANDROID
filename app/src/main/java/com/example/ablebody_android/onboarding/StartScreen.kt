package com.example.ablebody_android.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.R
import com.example.ablebody_android.utils.CustomButton
import com.example.ablebody_android.utils.HighlightText
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.SmallTextGrey


@Composable
private fun StartContentLayout(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 130.dp)
    ) {
        HighlightText(
            string = "나만의 운동 스타일을 찾다\n애블바디",
            colorStringList = listOf("애블바디"),
            color = AbleBlue,
            style = TextStyle(
                fontSize = 27.sp,
                color = AbleDark,
                lineHeight = 40.sp,
                fontFamily = FontFamily(Font(resId = R.font.noto_sans_cjkr_black))
            ),
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
        )

        HighlightText(
            string = "운동에 진심인 사람들의\n다양한 운동 스타일을 만나보세요.",
            colorStringList = listOf("다양한 운동 스타일"),
            color = AbleBlue,
            style = TextStyle(
                fontSize = 17.sp,
                color = SmallTextGrey,
                lineHeight = 27.2.sp,
                fontFamily = FontFamily(Font(resId = R.font.noto_sans_cjkr_black))
            ),
            modifier = Modifier.padding(vertical = 28.dp, horizontal = 16.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun StartContentLayoutPreview() {
    StartContentLayout()
}


@Composable
private fun StartBottomLayout(
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CustomButton(
            text = "시작하기",
            onClick = onClick,
        )

        Row(
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(text = "이미 계정이 있나요? ")
            HighlightText(
                string = "로그인",
                colorStringList = listOf("로그인"),
                color = AbleBlue,
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    /* TODO: (온보딩/시작하기) 로그인 버튼 클릭 후 이벤트 */
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartBottomLayoutPreview() {
    StartBottomLayout() {  }
}


@Composable
fun StartScreen(
    startButtonOnClick: () -> Unit
) {
    Scaffold(
        bottomBar = { StartBottomLayout(startButtonOnClick) },
    ) {
        StartContentLayout(modifier = Modifier.padding(it))
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    StartScreen() {}
}