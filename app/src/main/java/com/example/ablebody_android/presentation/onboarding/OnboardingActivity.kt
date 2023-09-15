package com.example.ablebody_android.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.asLiveData
import com.example.ablebody_android.presentation.main.MainActivity
import com.example.ablebody_android.presentation.onboarding.data.CertificationNumberInfoMessageUiState
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class OnboardingActivity : ComponentActivity() {
    private val onboardingViewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ABLEBODY_AndroidTheme {
                // A surface container using the 'background' color from the theme
                OnboardingManager(viewModel = onboardingViewModel)
            }
        }

        onboardingViewModel.certificationNumberInfoMessageUiState.asLiveData().observe(this) {
            if (it == CertificationNumberInfoMessageUiState.Already) {
                startMainActivity()
            }
        }

        onboardingViewModel.createNewUser.distinctUntilChanged().asLiveData().observe(this) {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        Log.d("TEST", "startMainActivity")
        val intent = Intent(this@OnboardingActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}