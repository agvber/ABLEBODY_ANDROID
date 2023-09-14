package com.example.ablebody_android.ui.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ablebody_android.ui.theme.AbleBlue

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        modifier = modifier.fillMaxWidth().padding(16.dp).height(55.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AbleBlue),
        enabled = enable
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun CustomButtonPreview() {
    CustomButton(text = "시작하기") {

    }
}