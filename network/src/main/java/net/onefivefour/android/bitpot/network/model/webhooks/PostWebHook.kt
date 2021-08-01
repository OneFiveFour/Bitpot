package net.onefivefour.android.bitpot.network.model.webhooks
import com.google.gson.annotations.SerializedName

/**
 * The data class representing a WebHook as it should be sent in POST or PUT calls.
 */
data class PostWebHook(
    @SerializedName("active")
    val active: Boolean,
    @SerializedName("description")
    val description: String,
    @SerializedName("events")
    val events: List<String>?,
    @SerializedName("url")
    val url: String
)