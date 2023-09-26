package com.example.ablebody_android.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.R
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.White

@Composable
fun BackButtonTopBarLayout(
    onBackRequest: () -> Unit,
    titleText: String = "",
    modifier: Modifier = Modifier,
    actions: @Composable() (RowScope.() -> Unit) = {}
) {
    AppBar(
        backgroundColor = White,
        contentColor = White,
        ContentPadding,
        RectangleShape,
        modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CompositionLocalProvider(
                LocalContentAlpha provides ContentAlpha.high,
            ) {
                Box(
                    modifier = modifier
                        .clickable(
                            onClick = onBackRequest,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_appbar_back_button),
                        contentDescription = "back button"
                    )
                }
            }
        }

        Row(
            Modifier.fillMaxHeight().weight(1f).padding(horizontal = 35.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.h6) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high
                ) {
                    Text(
                        text = titleText,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_regular)),
                            fontWeight = FontWeight(400),
                            color = AbleDark,
                        )
                    )
                }
            }
        }

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Row(
                Modifier.fillMaxHeight(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                content = actions
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BackButtonTopBarLayoutPreview1() {
    ABLEBODY_AndroidTheme {
        BackButtonTopBarLayout(onBackRequest = {  })
    }
}

@Preview(showBackground = true)
@Composable
private fun BackButtonTopBarLayoutPreview2() {
    ABLEBODY_AndroidTheme {
        BackButtonTopBarLayout(
            titleText = "hi",
            onBackRequest = {  }
        )
    }
}


@Composable
private fun AppBar(
    backgroundColor: Color,
    contentColor: Color,
    contentPadding: PaddingValues,
    shape: Shape,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shape = shape,
        modifier = modifier
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Row(
                Modifier.fillMaxWidth()
                    .padding(contentPadding)
                    .height(56.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}


private val AppBarHorizontalPadding = 16.dp
private val ContentPadding = PaddingValues(
    start = AppBarHorizontalPadding,
    end = AppBarHorizontalPadding
)