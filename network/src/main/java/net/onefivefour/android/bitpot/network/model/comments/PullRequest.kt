package net.onefivefour.android.bitpot.network.model.comments

import com.google.gson.annotations.SerializedName

/**
 * The basic model class for a PullRequest.
 */
data class PullRequest(
    @SerializedName("id")
    val id: Int
)