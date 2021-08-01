package net.onefivefour.android.bitpot.network.model.downloads

import com.google.gson.annotations.SerializedName

/**
 * This data class contains all links delivered
 * with the api call
 */
data class Links(
    @SerializedName("self")
    val self: Link
)