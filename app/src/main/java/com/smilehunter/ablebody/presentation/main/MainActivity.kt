package com.smilehunter.ablebody.presentation.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.smilehunter.ablebody.presentation.main.ui.MainScreen
import com.smilehunter.ablebody.presentation.onboarding.OnboardingActivity
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.tosspayments.paymentsdk.PaymentWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        firebaseAnalytics = Firebase.analytics

        val isNetworkConnectionFlow = callbackFlow { networkState() }
            .stateIn(
                scope = lifecycleScope,
                started = SharingStarted.Eagerly,
                initialValue = true
            )

        val paymentWidget = PaymentWidget(
            activity = this,
            clientKey = "test_ck_ALnQvDd2VJYq55dEqlb3Mj7X41mN",
            customerKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6"
        )

        setContent {
            ABLEBODY_AndroidTheme {
                MainScreen(
                    isNetworkConnectionFlow = isNetworkConnectionFlow,
                    paymentWidget = paymentWidget
                )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "애블바디 알림"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("AblebodyNotificationChannelId", name, importance)
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

    private suspend fun ProducerScope<Boolean>.networkState() {
        val connectivityManager = getSystemService<ConnectivityManager>()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySendBlocking(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySendBlocking(false)
            }
        }

        connectivityManager?.registerDefaultNetworkCallback(networkCallback)

        awaitClose { connectivityManager?.unregisterNetworkCallback(networkCallback) }
    }
}