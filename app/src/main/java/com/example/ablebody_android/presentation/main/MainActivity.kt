package com.example.ablebody_android.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.ablebody_android.presentation.main.ui.MainScreen
import com.example.ablebody_android.presentation.onboarding.OnboardingActivity
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!viewModel.hasAuthToken) {
            startOnboardingActivity()
        }

        setContent {
            ABLEBODY_AndroidTheme {
                MainScreen()
            }
        }

        viewModel.responseInvalidRefreshToken.observe(this) {
            startOnboardingActivity()
        }
    }

    private fun startOnboardingActivity() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        finish()
    }
}