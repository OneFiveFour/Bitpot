package net.onefivefour.android.bitpot.network.model.workspace

import com.google.gson.annotations.SerializedName

/**
 * A workspace of the current user.
 */
data class Workspace(
    @SerializedName("links")
    val links: Links,
    @SerializedName("name")
    val displayName: String,
    @SerializedName("uuid")
    val uuid: String
)