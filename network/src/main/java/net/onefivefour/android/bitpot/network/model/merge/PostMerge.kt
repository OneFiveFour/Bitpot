package net.onefivefour.android.bitpot.network.model.merge

import com.google.gson.annotations.SerializedName

/**
 * The network model class to merge a pull request as a POST request
 */
data class PostMerge(
    @SerializedName("message")
    val commitMessage: String,
    @SerializedName("close_source_branch")
    val closeSourceBranch: Boolean,
    @SerializedName("merge_strategy")
    val mergeStrategy: String
)
