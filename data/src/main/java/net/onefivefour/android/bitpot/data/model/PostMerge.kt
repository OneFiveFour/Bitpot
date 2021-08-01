package net.onefivefour.android.bitpot.data.model

/**
 * The data class for sending a pull request merge to the server.
 */
data class PostMerge(
    val commitMessage: String,
    val closeSourceBranch: Boolean,
    val mergeStrategy: MergeStrategy
)