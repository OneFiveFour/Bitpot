package net.onefivefour.android.bitpot.network.model.pullrequest

import com.google.gson.annotations.SerializedName

/**
 * A participant of a pull request.
 */
data class Participant(
    @SerializedName("approved")
    val approved: Boolean,
    @SerializedName("user")
    val user: User
)