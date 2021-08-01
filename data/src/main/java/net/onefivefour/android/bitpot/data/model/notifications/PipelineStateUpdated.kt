package net.onefivefour.android.bitpot.data.model.notifications

import com.google.gson.annotations.SerializedName

/**
 * The class describing the payload of pipeline state update notification.
 * This class extends [NotificationData]. Therefore Gson is using the 'data' field of
 * the incoming payload to deserialize this PipelineStateUpdate.
 * For more information see [BitpotMessagingService#createGson()] in the app module as
 * well as the RuntimeTypeAdapterFactory used in the koin [appModule]
 */
class PipelineStateUpdated(
    @SerializedName("repositoryName")
    val repositoryName: String,
    @SerializedName("workspaceUuid")
    val workspaceUuid: String,
    @SerializedName("repositoryUuid")
    val repositoryUuid: String,
    @SerializedName("pipelineState")
    val pipelineState: String
) : NotificationData()