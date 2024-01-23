package com.smilehunter.ablebody

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.smilehunter.ablebody.data.repository.FCMSyncRepository
import com.smilehunter.ablebody.data.repository.TokenRepository
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import com.smilehunter.ablebody.ui.theme.AbleBlue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AbleBodyFirebaseMessagingService: FirebaseMessagingService() {

    @Inject @Dispatcher(AbleBodyDispatcher.IO) lateinit var ioDispatcher: CoroutineDispatcher
    @Inject lateinit var fcmSyncRepository: FCMSyncRepository
    @Inject lateinit var tokenRepository: TokenRepository

    private val job = Job()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN", token)
        if (tokenRepository.hasToken) {
            CoroutineScope(ioDispatcher + job).launch {
                try {
                    fcmSyncRepository.updateFCMTokenAndAppVersion(token, BuildConfig.VERSION_NAME)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val builder = NotificationCompat.Builder(this, "AblebodyNotificationChannelId")
            .setSmallIcon(R.drawable.ablebody_notification_logo)
            .setColor(AbleBlue.toArgb())
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)


        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(
                    this@AbleBodyFirebaseMessagingService,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(100, builder.build())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}