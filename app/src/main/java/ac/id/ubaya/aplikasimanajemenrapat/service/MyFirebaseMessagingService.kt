package ac.id.ubaya.aplikasimanajemenrapat.service

import ac.id.ubaya.aplikasimanajemenrapat.R
import ac.id.ubaya.aplikasimanajemenrapat.ui.main.MainActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
        Log.d(TAG, "Message data payload: " + remoteMessage.data)
        Log.d(TAG, "Message Notification Body: ${remoteMessage.notification?.body}")

        val data = remoteMessage.data

        val title: String
        val message: String

        when (data["type"]) {
            "1" -> {
                title = getString(R.string.meeting_reminder_title)
                message = if (data["days_left"] == "0") {
                    getString(R.string.meeting_day_reminder, data["title"])
                } else {
                    getString(R.string.meeting_reminder, data["title"], data["days_left"])
                }
            }
            "2" -> {
                title = getString(R.string.meeting_started)
                message = getString(R.string.meeting_started_notification, data["title"])
            }
            "3" -> {
                title = getString(R.string.meeting_ended)
                message = getString(R.string.meeting_ended_notification, data["title"], data["exp"])
            }
            "4" -> {
                val rawName = data["title"].toString()
                val split = rawName.split("||")

                val name = if (Locale.getDefault().language == "id")
                        split[0]
                    else
                        split[1]

                title = getString(R.string.achievement_get_title)
                message = getString(R.string.achievement_get_notification, name, data["exp"])
            }
            "5" -> {
                title = getString(R.string.level_up_title)
                message = getString(R.string.level_up_notification, data["level"])
            }
            "6" -> {
                title = getString(R.string.meeting_invitation_title)
                message = getString(R.string.meeting_invitation_notification, data["title"], data["organization"])
            }
            "7" -> {
                val statusTask = data["status"]
                title = getString(R.string.task_completed_notification_title)
                message = if (statusTask == "1") {
                    getString(R.string.task_completed_on_time_notification, data["title"], data["exp"])
                } else {
                    getString(R.string.task_completed_late_notification, data["title"])
                }
            }
            else -> {
                title = remoteMessage.notification?.title.toString()
                message = remoteMessage.notification?.body.toString()
            }
        }

        sendNotification(title, message)
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val contentIntent = Intent(applicationContext, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    companion object {
        private val TAG = MyFirebaseMessagingService::class.java.simpleName
        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_CHANNEL_ID = "Firebase Channel"
        private const val NOTIFICATION_CHANNEL_NAME = "Firebase Notification"
    }
}