package com.example.tfhmobile

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseNotificationService : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title
        val body = message.notification?.body
        val channelId = "NOTIFICATION_CHANNEL"
        val channel = NotificationChannel(channelId, "Notification channel", NotificationManager.IMPORTANCE_HIGH)
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        val builder = Notification.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.tfh_logo)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notification))
            .setAutoCancel(true)

        NotificationManagerCompat.from(this).notify(1, builder.build())
        super.onMessageReceived(message)

    }
}