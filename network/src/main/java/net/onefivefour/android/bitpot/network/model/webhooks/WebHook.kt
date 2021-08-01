package net.onefivefour.android.bitpot.network.model.webhooks
import com.google.gson.annotations.SerializedName

/**
 * The network model of a WebHook
 */
data class WebHook(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("events")
    val events: List<String>,
    @SerializedName("subject")
    val subject: Subject,
    @SerializedName("url")
    val url: String,
    @SerializedName("uuid")
    val uuid: String
)