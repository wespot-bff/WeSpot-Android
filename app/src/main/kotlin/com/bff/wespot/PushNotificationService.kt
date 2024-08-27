package com.bff.wespot

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bff.wespot.common.CHANNEL_ID
import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.util.DataStoreKey.PUSH_TOKEN
import com.bff.wespot.splash.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PushNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var dataStore: DataStoreRepository

    @Inject
    lateinit var coroutineDispatcher: CoroutineDispatcher
    private val coroutineScope by lazy { CoroutineScope(coroutineDispatcher) }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        coroutineScope.launch {
            dataStore.saveString(PUSH_TOKEN, token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.data.isNotEmpty() || message.notification != null) {
            sendNotification(message)
        }
    }

    private fun sendNotification(message: RemoteMessage) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationId: Int = (System.currentTimeMillis()).toInt()

        val intent = Intent(this, SplashActivity::class.java)
        for (key in message.data.keys) {
            Timber.d("$key to ${message.data[key]}")
            intent.putExtra(key, message.data[key])
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationId,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
        )

        val title = message.notification?.title
        val content = message.notification?.body
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_notification)
            .setColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.ic_launcher_background,
                )
            )
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}
