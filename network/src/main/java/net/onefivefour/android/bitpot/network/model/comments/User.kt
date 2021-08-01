package net.onefivefour.android.bitpot.network.model.comments

import com.google.gson.annotations.SerializedName

/**
 * The user that created a pull request [Comment]
 */
data class User(
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("links")
    val links: Links,
    @SerializedName("account_id")
    val accountId: String
)