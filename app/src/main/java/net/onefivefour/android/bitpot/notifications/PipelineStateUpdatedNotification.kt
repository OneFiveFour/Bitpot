package net.onefivefour.android.bitpot.notifications

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.core.app.TaskStackBuilder
import net.onefivefour.android.bitpot.R
import net.onefivefour.android.bitpot.data.model.PipelineState
import net.onefivefour.android.bitpot.data.model.notifications.PipelineStateUpdated
import net.onefivefour.android.bitpot.screens.repository.RepositoryActivity
import net.onefivefour.android.bitpot.screens.repository.RepositoryActivityArgs

/**
 * This Notification informs the user about an update of a Pipeline.
 * To generate the Notification it uses the data coming from [PipelineStateUpdated]
 */
class PipelineStateUpdatedNotification : NotificationSender<PipelineStateUpdated>() {

    override fun getNotificationId(): NotificationId {
        return NotificationId.PIPELINE_STATE_UPDATED
    }

    override fun getChannelId(): String {
        return context.getString(R.string.channel_id_pipeline_updates)
    }

    override fun getChannelName(): String {
        return context.getString(R.string.channel_name_pipeline_updates)
    }

    override fun getNotificationTitle(notificationData: PipelineStateUpdated): String {
        return context.getString(R.string.notification_title_pipeline_state_updated, notificationData.repositoryName)
    }

    @Suppress("ComplexMethod")
    override fun getNotificationContent(notificationData: PipelineStateUpdated): String {
        val contentResId = when (PipelineState.valueOf(notificationData.pipelineState)) {
            PipelineState.PENDING -> R.string.pipeline_is_pending
            PipelineState.IN_PROGRESS -> R.string.pipeline_is_now_running
            PipelineState.PAUSED -> R.string.pipeline_paused
            PipelineState.SUCCESSFUL -> R.string.pipeline_was_successful
            PipelineState.STOPPED -> R.string.pipeline_stopped
            PipelineState.EXPIRED -> R.string.pipeline_expired
            PipelineState.FAILED -> R.string.pipeline_failed
            PipelineState.ERROR -> R.string.pipeline_has_an_error
            PipelineState.UNKNOWN -> 0
        }
        return context.getString(contentResId)
    }

    override fun getPendingIntent(notificationData: PipelineStateUpdated): PendingIntent {
        val resultIntent = Intent(context, RepositoryActivity::class.java)
        val bundle = getBundle(notificationData)
        resultIntent.putExtras(bundle)

        val requestCode = 12 * notificationData.hashCode()
        return TaskStackBuilder
            .create(context)
            .addNextIntentWithParentStack(resultIntent)
            .getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT) ?: throw IllegalStateException("Could not create Pending Intent for Pipelines Notification")
    }


    private fun getBundle(notificationData: PipelineStateUpdated): Bundle {
        val argsBuilder = RepositoryActivityArgs.Builder(notificationData.workspaceUuid, notificationData.repositoryUuid)
        argsBuilder.targetView = "pipelines"
        argsBuilder.targetId = ""
        return argsBuilder.build().toBundle()
    }

}