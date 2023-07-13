package com.example.ablebody_android.onboarding.utils.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.SmallTextGrey
import com.example.ablebody_android.ui.theme.White

// TODO: disable경우 포함
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    labelText: String,
    value: String,
    onValueChange: (String) -> Unit,
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
        label = {
            Text(
                text = labelText,
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight(400),
                    color = SmallTextGrey,
                )
            )
        },
        textStyle = TextStyle(
            fontSize = 22.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight(700),
            color = AbleDark,
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = if(labelText == "닉네임") {
                                KeyboardType.Text
                        } else {
                            KeyboardType.Number
                        }),
    )
}

@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    CustomTextField(
        value = "", labelText = "휴대폰 번호"
    ) {
    }
}