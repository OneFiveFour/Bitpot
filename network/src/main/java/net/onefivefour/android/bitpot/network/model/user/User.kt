package net.onefivefour.android.bitpot.network.model.user

import com.google.gson.annotations.SerializedName

/**
 * A User object to store the accountId after the login
 */
data class User(
    @SerializedName("account_id")
    val accountId: String
)