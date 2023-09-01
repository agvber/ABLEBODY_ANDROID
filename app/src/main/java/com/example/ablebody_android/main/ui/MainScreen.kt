package com.example.ablebody_android.main.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ablebody_android.brand.BrandViewModel
import com.example.ablebody_android.brand.ui.BrandScreen
import com.example.ablebody_android.utils.ItemSearchBar

@Composable
fun MainScreen(viewModel: BrandViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    Scaffold(
        topBar = {
            ItemSearchBar(
                textFiledOnClick = { /*TODO*/ },
                alertOnClick = { /*TODO*/ }
            )
        },
        bottomBar = { MainNavigationBar() },
        content = {
            BrandScreen(
                modifier = Modifier.padding(it),
                viewModel = viewModel
            )
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}