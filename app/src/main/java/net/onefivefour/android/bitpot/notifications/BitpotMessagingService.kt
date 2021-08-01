package net.onefivefour.android.bitpot.notifications

import androidx.paging.ExperimentalPagingApi
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.common.UpdateFirebaseTokenWorker
import net.onefivefour.android.bitpot.data.model.notifications.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * The FirebaseMessagingService that receives incoming Notifications.
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class BitpotMessagingService : FirebaseMessagingService(), KoinComponent {

    private val gson: Gson by inject()

    private val workManager : WorkManager by inject()

    /**
     * Process the given [RemoteMessage].
     * Its 'data' field is used to decide what kind of notification it contains.
     * Depending on that a notification is created for the right notification-channel.
     *
     * @param remoteMessage the FCM message received.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (!BitpotData.hasAccessToken()) return

        val messageData = remoteMessage.data["data"]

        when (val notificationData = gson.fromJson(messageData, NotificationData::class.java)) {
            is PipelineStateUpdated -> PipelineStateUpdatedNotification().send(notificationData)
            is PullRequestCreated -> PullRequestCreatedNotification().send(notificationData)
            is PullRequestCommentCreated -> PullRequestCommentCreatedNotification().send(notificationData)
            is PullRequestApproved -> PullRequestApprovedNotification().send(notificationData)
            is PullRequestUnapproved -> PullRequestUnapprovedNotification().send(notificationData)
            else -> throw IllegalArgumentException("Unknown notification message data received")
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Timber.d("+++ Got new Firebase Token in BitpotMessagingService: $token")

        // updates existing web hooks in database and update token stored in BitpotData
        val workRequest = UpdateFirebaseTokenWorker.buildRequest(token)
        workManager.enqueue(workRequest)
    }
}
