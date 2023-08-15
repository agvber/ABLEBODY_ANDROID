package com.example.ablebody_android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.ablebody_android.onboarding.ui.OnboardingActivity
import com.example.ablebody_android.utils.LogoImage

class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition { true }

        startActivity(Intent(this@SplashScreenActivity, OnboardingActivity::class.java))
        finish()
    }
}

@Composable
fun SplashPartLayout() {
    LogoImage()
}


@Preview(showBackground = true)
@Composable
fun SplashPartLayoutPreview() {
    LogoImage()
}

