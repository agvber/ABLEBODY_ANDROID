package com.example.ablebody_android.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.ablebody_android.ui.theme.AbleBlue

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