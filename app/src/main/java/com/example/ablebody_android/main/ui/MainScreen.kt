package com.example.ablebody_android.main.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ablebody_android.brand.ui.BrandNavHost
import com.example.ablebody_android.utils.ItemSearchBar

@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            ItemSearchBar(
                textFiledOnClick = { /*TODO*/ },
                alertOnClick = { /*TODO*/ }
            )
        },
        bottomBar = { MainNavigationBar() },
        content = { paddingValue ->
            Surface(
                modifier = Modifier.padding(paddingValue)
            ) {
                BrandNavHost()
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}