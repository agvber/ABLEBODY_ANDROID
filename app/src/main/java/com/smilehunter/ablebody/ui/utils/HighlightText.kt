package com.smilehunter.ablebody.ui.utils

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.smilehunter.ablebody.ui.theme.AbleBlue

@Composable
fun HighlightText(
    string: String,
    colorStringList: List<String>,
    color: Color,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default
) {
    val colorCharIndexSet = hashSetOf<Int>()

    for (colorString in colorStringList) {
        val firstIndex = string.indexOf(colorString)
        val lastIndex = firstIndex + (colorString.length - 1)
        colorCharIndexSet.addAll(firstIndex..lastIndex)
    }

    Text(
        text = buildAnnotatedString {
            for (index in string.indices) {
                if (colorCharIndexSet.contains(index)) {
                    withStyle(SpanStyle(color)) { append(string[index]) }
                } else {
                    append(string[index])
                }
            }
        },
        modifier = modifier,
        style = style,
    )
}

@Preview(showBackground = true)
@Composable
fun HighlightTextPreview() {
    HighlightText(
        string = "운동 기록이 쉬워진다.\n애블바디",
        colorStringList = listOf("운동", "애블바디"),
        color = AbleBlue
    )
}

@Composable
fun HighlightText(
    modifier: Modifier = Modifier,
    string: String,
    applyStringList: List<String>,
    applySpanStyle: SpanStyle,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    val colorCharIndexSet = hashSetOf<Int>()

    for (colorString in applyStringList) {
        val firstIndex = string.indexOf(colorString)
        val lastIndex = firstIndex + (colorString.length - 1)
        colorCharIndexSet.addAll(firstIndex..lastIndex)
    }

    Text(
        text = buildAnnotatedString {
            for (index in string.indices) {
                if (colorCharIndexSet.contains(index)) {
                    withStyle(applySpanStyle) { append(string[index]) }
                } else {
                    append(string[index])
                }
            }
        },
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        inlineContent = inlineContent,
        onTextLayout = onTextLayout,
        style = style
    )
}