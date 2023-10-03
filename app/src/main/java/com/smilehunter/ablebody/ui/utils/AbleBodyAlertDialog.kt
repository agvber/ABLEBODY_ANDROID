package com.smilehunter.ablebody.ui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.smilehunter.ablebody.R
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDark
import com.smilehunter.ablebody.ui.theme.LightShaded
import com.smilehunter.ablebody.ui.theme.White

@Composable
fun AbleBodyAlertDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    positiveText: String? = null,
    positiveButtonOnClick: () -> Unit,
    negativeText: String? = null,
    negativeButtonOnClick: () -> Unit,
    content: (@Composable () -> Unit),
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(size = 10.dp),
            color = backgroundColor,
            contentColor = contentColor
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .padding(top = 20.dp, bottom = 15.dp)
            ) {
                content()
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    if (negativeText != null) {
                        AbleBodyAlertDialogButton(
                            onClick = negativeButtonOnClick,
                            modifier = Modifier.weight(1f),
                            isPositive = false,
                            text = negativeText
                        )
                    }
                    if (positiveText != null) {
                        AbleBodyAlertDialogButton(
                            onClick = positiveButtonOnClick,
                            modifier = Modifier.weight(1f),
                            isPositive = true,
                            text = positiveText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AbleBodyAlertDialogButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPositive: Boolean = true,
    text: String
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPositive) AbleBlue else LightShaded
        ),
        shape = RoundedCornerShape(10.dp),
    ) {
        if (isPositive) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                    fontWeight = FontWeight(500),
                    color = White,
                    textAlign = TextAlign.Center,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp)
            )
        } else {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.noto_sans_cjk_kr_medium)),
                    fontWeight = FontWeight(500),
                    color = AbleBlue,
                    textAlign = TextAlign.Center,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AbleBodyAlertDialogButtonPreview() {
    AbleBodyAlertDialogButton(onClick = {}, text = "업데이트")
}

@Preview(showSystemUi = true)
@Composable
fun AbleBodyAlertDialogPreview() {
    ABLEBODY_AndroidTheme {
        AbleBodyAlertDialog(
            onDismissRequest = {},
            positiveText = "재시도",
            positiveButtonOnClick = {},
            negativeText = "설정",
            negativeButtonOnClick = {},
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
}