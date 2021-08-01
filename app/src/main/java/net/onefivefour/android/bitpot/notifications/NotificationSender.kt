package net.onefivefour.android.bitpot.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import net.onefivefour.android.bitpot.R
import org.koin.core.KoinComponent
import org.koin.core.inject


/**
 * This abstract class should be extended for every kind of Notification (e.g. PipelineUpdates or PullRequestUpdates).
 * The extending class can then be used to simply call the [send] method to create and send a Notification using the given notificationData.
 */
abstract class NotificationSender<T> : KoinComponent {

    internal val context: Context by inject()

    fun send(notificationData: T) {

        val notificationId = getNotificationId()
        val channelId = getChannelId()
        val channelName = getChannelName()
        val title = getNotificationTitle(notificationData)
        val content = getNotificationContent(notificationData)
        val pendingIntent = getPendingIntent(notificationData)

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // build notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)
            .setChannelId(channelId)
            .build()


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // ensure notification channel on Oreo+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId.ordinal, notification)
    }


    /**
     * @return The [NotificationId] of this notification.
     * If two notifications with the same Id arrive or an old one was not cancelled yet, the older one gets deleted.
     */
    abstract fun getNotificationId(): NotificationId

    /**
     * @return The id of the notification channel.
     * Each app can define its own notification channels which can be turned on/off by the user
     */
    abstract fun getChannelId(): String

    /**
     * @return The name of the notification channel. This name is visible in the UI!
     * Each app can define its own notification channels which can be turned on/off by the user
     */
    abstract fun getChannelName(): String

    /**
     * @return The title of the notification.
     */
    abstract fun getNotificationTitle(notificationData: T): String

    /**
     * @return The content of the notification.
     */
    abstract fun getNotificationContent(notificationData: T): String

    /**
     * @return Creates the pending intent that is fired when the user clicks on the notification.
     */
    abstract fun getPendingIntent(notificationData: T): PendingIntent
}
