package com.smilehunter.ablebody.ui.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilehunter.ablebody.ui.theme.AbleBlue
import com.smilehunter.ablebody.ui.theme.AbleDeep
import com.smilehunter.ablebody.ui.theme.White

@Composable
fun DisableCustomWithLabelTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelText: @Composable() (() -> Unit)? = null,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = TextFieldDefaults.colors(
            disabledContainerColor = White,
            errorContainerColor = White,
            unfocusedContainerColor = White,
            focusedContainerColor = White,
            focusedIndicatorColor = AbleBlue,
        ),
        enabled = false,
        label = labelText,
        textStyle = TextStyle(
            fontSize = 22.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight(700),
            color = AbleDeep,
        ),
    )
}

//아래처럼 CustomTextField 쓰고 색만 바꿀 수 없나?ㅜ

//@Composable
//fun DisableCustomWithLabelTextField(
//    modifier: Modifier = Modifier,
//    value: String,
//    onValueChange: (String) -> Unit,
//    labelText: @Composable() (() -> Unit)? = null,
//) {
//    CustomTextField(
//        value = value,
//        labelText = labelText,
//        onValueChange = onValueChange
//    )
//}

@Preview(showBackground = true)
@Composable
fun DisableCustomWithLabelTextFieldPreview1() {
    DisableCustomWithLabelTextField(
        value = "nahyi",
        onValueChange = {  },
        labelText = { Text(text = "닉네임") }
    )
}

@Preview(showBackground = true)
@Composable
fun DisableCustomWithLabelTextFieldPreview() {
    DisableCustomWithLabelTextField(
        value = "01092393487",
        onValueChange = {  },
        labelText = { Text(text = "휴대폰 번호") }
    )
}

