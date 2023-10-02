package com.smilehunter.ablebody.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.asLiveData
import com.smilehunter.ablebody.presentation.main.MainActivity
import com.smilehunter.ablebody.presentation.onboarding.data.CertificationNumberInfoMessageUiState
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
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

        onboardingViewModel.certificationNumberInfoMessageUiState.asLiveData().observe(this) { result ->
            if (result is CertificationNumberInfoMessageUiState.Already) {
                startMainActivity()
            }
        }

        onboardingViewModel.createNewUser.distinctUntilChanged().asLiveData().observe(this) {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this@OnboardingActivity, MainActivity::class.java)
        startActivity(intent)
        onboardingViewModel.updateFCMTokenAndAppVersion()
        finish()
    }
}