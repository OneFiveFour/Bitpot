package net.onefivefour.android.bitpot.network.model.pullrequests

import com.google.gson.annotations.SerializedName

/**
 * An instance of this class describes the destination
 * branch of a [PullRequest]
 */
data class Destination(
    @SerializedName("branch")
    val branch: Branch,
    @SerializedName("repository")
    val repository: Repository
)