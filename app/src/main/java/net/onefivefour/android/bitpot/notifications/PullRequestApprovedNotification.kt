package net.onefivefour.android.bitpot.notifications

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.core.app.TaskStackBuilder
import androidx.paging.ExperimentalPagingApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.data.model.notifications.PullRequestApproved
import net.onefivefour.android.bitpot.screens.pullrequest.PullRequestActivity
import net.onefivefour.android.bitpot.screens.pullrequest.PullRequestActivityArgs

/**
 * This Notification informs the user about the approval of a pull request.
 * To generate the Notification it uses the data coming from [PullRequestApproved]
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class PullRequestApprovedNotification : NotificationSender<PullRequestApproved>() {
    override fun getNotificationId(): NotificationId {
        return NotificationId.PULL_REQUEST_APPROVED
    }

    override fun getChannelId(): String {
        return context.getString(R.string.channel_id_pull_request_updates)
    }

    override fun getChannelName(): String {
        return context.getString(R.string.channel_name_pull_request_updates)
    }

    override fun getNotificationTitle(notificationData: PullRequestApproved): String {
        return context.getString(R.string.notification_title_pull_request_approved)
    }

    override fun getNotificationContent(notificationData: PullRequestApproved): String {
        return context.getString(
            R.string.notification_content_pull_request_approved,
            notificationData.displayName,
            notificationData.repositoryName
        )
    }

    override fun getPendingIntent(notificationData: PullRequestApproved): PendingIntent {
        val resultIntent = Intent(context, PullRequestActivity::class.java)
        val bundle = getBundle(notificationData)
        resultIntent.putExtras(bundle)

        val requestCode = 12 * notificationData.hashCode()
        return TaskStackBuilder
            .create(context)
            .addNextIntentWithParentStack(resultIntent)
            .getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT) ?: throw IllegalStateException("Could not create Pending Intent for Approval Notification")
    }

    private fun getBundle(notificationData: PullRequestApproved): Bundle {
        val argsBuilder = PullRequestActivityArgs.Builder(
            notificationData.workspaceUuid,
            notificationData.repositoryUuid,
            notificationData.pullRequestId
        )
        return argsBuilder.build().toBundle()
    }

}