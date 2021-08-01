package net.onefivefour.android.bitpot.data.model.notifications

import com.google.gson.annotations.SerializedName

/**
 * The class describing the payload of a notification about a new pull request.
 * This class extends [NotificationData]. Therefore Gson is using the 'data' field of
 * the incoming payload to deserialize this payload.
 * For more information see [BitpotMessagingService#createGson()] in the app module as
 * well as the RuntimeTypeAdapterFactory used in the koin [appModule]
 */
class PullRequestCreated(
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("pullRequestId")
    val pullRequestId: Int,
    @SerializedName("workspaceUuid")
    val workspaceUuid: String,
    @SerializedName("repositoryUuid")
    val repositoryUuid: String,
    @SerializedName("repositoryName")
    val repositoryName: String
) : NotificationData()