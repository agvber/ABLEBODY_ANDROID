package com.example.ablebody_android.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ablebody_android.R
import com.example.ablebody_android.ui.theme.AbleBlue
import kotlinx.coroutines.delay

@Composable
fun LogoImage() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Image(
            painter = painterResource(id = R.drawable.ablebodylogo),
            contentDescription = "Logo",
            modifier = Modifier
                .width(283.99927.dp)
                .height(41.71013.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LogoImagePreview() {
    LogoImage()
}