package com.example.ablebody_android.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.AbleDeep
import com.example.ablebody_android.ui.theme.White

@Composable
fun DisableCustomTextField(
    modifier: Modifier = Modifier,
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
        textStyle = TextStyle(
            fontSize = 22.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight(700),
            color = AbleDeep,
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun DisableCustomTextFieldPreview() {
    DisableCustomTextField(
        value = "01092393487",
        onValueChange = {  }
    )
}