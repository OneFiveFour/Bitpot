package net.onefivefour.android.bitpot.network.model.pullrequests

import com.google.gson.annotations.SerializedName

/**
 * This class describes the source branch of a [PullRequest]
 */
data class Source(
    @SerializedName("branch")
    val branch: Branch
)