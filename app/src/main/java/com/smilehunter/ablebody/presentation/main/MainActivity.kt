package com.smilehunter.ablebody.presentation.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.smilehunter.ablebody.presentation.main.ui.MainScreen
import com.smilehunter.ablebody.presentation.onboarding.OnboardingActivity
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics

        setContent {
            ABLEBODY_AndroidTheme {
                MainScreen()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alert" /* TODO 알림센터 이름 지정 */
            val descriptionText = "FCM" /* TODO 알림센터 설명 지정 */
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("ChanelId", name, importance).apply { /* TODO: 채널 ID 지정 */
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        lifecycleScope.launch {
            viewModel.responseInvalidRefreshToken.collectLatest {
                startOnboardingActivity()
            }
        }
    }

    private fun startOnboardingActivity() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        finish()
    }
}