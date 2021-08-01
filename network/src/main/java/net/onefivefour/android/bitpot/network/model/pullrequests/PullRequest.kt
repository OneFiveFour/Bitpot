package net.onefivefour.android.bitpot.network.model.pullrequests

import com.google.gson.annotations.SerializedName

/**
 * A pull request from the Bitbucket API
 */
data class PullRequest(
    @SerializedName("author")
    val author: Author,
    @SerializedName("destination")
    val destination: Destination,
    @SerializedName("id")
    val id: Int,
    @SerializedName("links")
    val links: Links,
    @SerializedName("source")
    val source: Source,
    @SerializedName("state")
    val state: String,
    @SerializedName("updated_on")
    val updatedOn: String
)