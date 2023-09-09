package com.example.ablebody_android.main.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import com.example.ablebody_android.main.MainNavHost

val scaffoldPaddingValueCompositionLocal = staticCompositionLocalOf {
    PaddingValues()
}

@Composable
fun MainScreen() {
    var isBottomBarShow by remember { mutableStateOf(true) }

    androidx.compose.material.Scaffold(
        bottomBar = {
            if (isBottomBarShow) {
                MainNavigationBar()
            }
        },
        content = { paddingValue ->
            CompositionLocalProvider(scaffoldPaddingValueCompositionLocal.provides(paddingValue)) {
                MainNavHost()
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}