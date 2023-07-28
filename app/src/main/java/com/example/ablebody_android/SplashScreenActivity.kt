package com.example.ablebody_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.ablebody_android.onboarding.OnboardingActivity
import com.example.ablebody_android.utils.LogoImage
import kotlinx.coroutines.delay

class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashPartLayout(this)
        }
    }
}

@Composable
fun SplashPartLayout(context: Context) {
    LogoImage()
    LaunchedEffect(Unit){
        delay(1000L)
        context.startActivity(Intent(context, OnboardingActivity::class.java))
    }
}


@Preview(showBackground = true)
@Composable
fun SplashPartLayoutPreview() {
    LogoImage()
}

