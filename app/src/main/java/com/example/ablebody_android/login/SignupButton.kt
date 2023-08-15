package com.example.ablebody_android.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ablebody_android.ui.theme.AbleBlue
import com.example.ablebody_android.ui.theme.LightShaded
import com.example.ablebody_android.ui.theme.White

@Composable
fun SignupButton(
    text: String,
    isChecked: Boolean,
) {
    Button(modifier = Modifier
        .width(145.dp)
        .height(47.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isChecked) LightShaded else AbleBlue
        ),
        shape = RoundedCornerShape(10.dp),
        onClick = { /*TODO: 새로 가입할지 선택 이벤트*/ }
    ) {
        Text(text = text, color = if (isChecked) AbleBlue else White)
    }
}

@Preview(showBackground = true)
@Composable
fun SignupButtonSPreview() {
    SignupButton("아니오", true)
}

@Composable
fun SignupButtonLayout(
    text1: String,
    isChecked1: Boolean,
    text2: String,
    isChecked2: Boolean
) {
    Column {
        Row {
            SignupButton(text1, isChecked1)
            SignupButton(text2, isChecked2)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InputGenderLayoutPreview() {
    SignupButtonLayout("아니오", true, "예",false)
}