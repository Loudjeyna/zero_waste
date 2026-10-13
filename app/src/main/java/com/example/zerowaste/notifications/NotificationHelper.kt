package com.example.zerowaste.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.zerowaste.R

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "expiry_reminder_channel"
        const val CHANNEL_NAME = "تذكير الصلاحية"
        const val NOTIFICATION_ID = 1001
    }

    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "إشعارات للطعام الذي على وشك الانتهاء"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun showExpiryNotification(itemsText: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // أيقونة التطبيق الافتراضية
            .setContentTitle("تنبيه: طعام على وشك الانتهاء! 🍅")
            .setContentText(itemsText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(itemsText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, notification.build())
    }
}