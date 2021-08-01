package net.onefivefour.android.bitpot.network.model.pullrequest

import com.google.gson.annotations.SerializedName

/**
 * This data class contains all links delivered
 * with the api call
 */
data class Links(
    @SerializedName("avatar")
    val avatar: Link
)