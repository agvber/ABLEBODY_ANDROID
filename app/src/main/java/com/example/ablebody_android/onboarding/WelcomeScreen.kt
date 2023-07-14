package com.example.ablebody_android.onboarding

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.ablebody_android.R
import com.example.ablebody_android.utils.HighlightText
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.SmallTextGrey

@Composable
fun WelcomeScreen() {

    ConstraintLayout(
        modifier = Modifier
    ) {
        val (loginCompleteText, welcomeText) = createRefs()

        Text(
            text = "로그인 완료!",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                fontWeight = FontWeight(400),
                color = SmallTextGrey,
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.constrainAs(ref = loginCompleteText) {
                bottom.linkTo(welcomeText.top)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
            }
        )

        HighlightText(
            string = "ablebody님,\n환영합니다!",
            colorStringList = listOf("ablebody"),
            color = AbleBlue,
            style = TextStyle(
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.noto_sans_cjkr_black)),
                fontWeight = FontWeight(700),
                color = AbleDark,
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.constrainAs(ref = welcomeText) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen()
}