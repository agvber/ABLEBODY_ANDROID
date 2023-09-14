package com.example.ablebody_android.ui.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDark
import com.example.ablebody_android.ui.theme.White

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    labelText: @Composable() (() -> Unit)? = null,
    placeholder: @Composable() (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        enabled = enabled,
        placeholder = placeholder,
        colors = TextFieldDefaults.colors(
            disabledContainerColor = White,
            errorContainerColor = White,
            unfocusedContainerColor = White,
            focusedContainerColor = White,
            focusedIndicatorColor = AbleBlue,
        ),
        label = labelText,
        textStyle = TextStyle(
            fontSize = 22.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight(700),
            color = AbleDark,
        ),
        singleLine = singleLine,
        keyboardOptions = keyboardOptions
    )
}

@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    var state by remember { mutableStateOf("휴대폰 번호") }
    CustomTextField(value = state, onValueChange = { state = it })
}