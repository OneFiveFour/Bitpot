package net.onefivefour.android.bitpot.network.model.pullrequest

import com.google.gson.annotations.SerializedName

/**
 * A user as part of a pull request.
 */
data class User(
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("links")
    val links: Links
)