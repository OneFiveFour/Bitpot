package net.onefivefour.android.bitpot.network.model.pullrequest

import com.google.gson.annotations.SerializedName

/**
 * Details of a pull request.
 */
data class PullRequest(
    @SerializedName("id")
    val id: Int,
    @SerializedName("participants")
    val participants: List<Participant>,
    @SerializedName("rendered")
    val rendered: Rendered,
    @SerializedName("state")
    val state: String,
    @SerializedName("title")
    val title: String
)


