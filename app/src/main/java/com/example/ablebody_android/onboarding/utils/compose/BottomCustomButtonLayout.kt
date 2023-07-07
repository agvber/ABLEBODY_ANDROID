package com.example.ablebody_android.onboarding.utils.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
private fun BottomBar(
    enable: Boolean = true,
    text: String,
    onClick: () -> Unit
) {
    CustomButton(text = text, onClick = onClick, enable = enable)
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    BottomBar(text = "") {}
}

@Composable
fun BottomCustomButtonLayout(
    buttonText: String,
    onClick: () -> Unit,
    enable: Boolean = true,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.padding(top = 130.dp),
        bottomBar = { BottomBar(enable = enable, text = buttonText, onClick = onClick) },
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun BottomCustomButtonLayoutPreview() {
    BottomCustomButtonLayout("ok", {  }) {

    }
}