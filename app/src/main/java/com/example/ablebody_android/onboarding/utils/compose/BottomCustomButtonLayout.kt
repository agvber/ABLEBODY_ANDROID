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
private fun BottomBar() {
    CustomButton(
        text = "인증번호 받기",
        onClick = { /*TODO*/ }
    )
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    BottomBar()
}

@Composable
fun BottomCustomButtonLayout(
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.padding(top = 130.dp),
        bottomBar = { BottomBar() },
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun BottomCustomButtonLayoutPreview() {
    BottomCustomButtonLayout() {

    }
}