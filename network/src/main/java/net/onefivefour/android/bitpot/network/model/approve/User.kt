package net.onefivefour.android.bitpot.network.model.approve

import com.google.gson.annotations.SerializedName

/**
 * The user that approved a pull request
 */
data class User(
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("links")
    val links: Links
)