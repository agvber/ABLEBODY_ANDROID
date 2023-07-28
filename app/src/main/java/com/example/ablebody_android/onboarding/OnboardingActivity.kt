package com.example.ablebody_android.onboarding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme

class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ABLEBODY_AndroidTheme {
                // A surface container using the 'background' color from the theme
                OnboardingManager()
            }
        }
    }
}