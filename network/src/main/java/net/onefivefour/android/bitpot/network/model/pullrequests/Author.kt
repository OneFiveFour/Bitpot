package net.onefivefour.android.bitpot.network.model.pullrequests

import com.google.gson.annotations.SerializedName

/**
 * This class represents basically a User
 * It is used to show who created a [PullRequest]
 */
data class Author(
    @SerializedName("links")
    val links: Links
)