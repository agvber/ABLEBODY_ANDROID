package com.example.ablebody_android.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.R
import com.example.ablebody_android.brand.ui.BrandDetailTopBarLayout
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.SmallTextGrey
import com.example.ablebody_android.ui.theme.White

@Composable
fun BackButtonTopBarLayout(
    onClick: () -> Unit,
    titleText: String = "",
) {
    TopAppBar(
        title = {
            Text(
                text = titleText,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                    fontWeight = FontWeight(400),
                    color = AbleDark,
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_appbar_back_button),
                contentDescription = "back button",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick
                    )
            )
        },
        backgroundColor = White
    )
}

@Preview(showBackground = true)
@Composable
private fun BackButtonTopBarLayoutPreview1() {
    ABLEBODY_AndroidTheme {
        BackButtonTopBarLayout(onClick = { /*TODO*/ })
    }
}

@Preview(showBackground = true)
@Composable
private fun BackButtonTopBarLayoutPreview2() {
    ABLEBODY_AndroidTheme {
        BackButtonTopBarLayout(
            titleText = "hi",
            onClick = { /*TODO*/ }
        )
    }
}
