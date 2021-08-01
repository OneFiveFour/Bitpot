package net.onefivefour.android.bitpot.data.model

/**
 * An enum of all possible merge strategies for a pull request.
 */
enum class MergeStrategy {
    MERGE_COMMIT,
    SQUASH,
    FAST_FORWARD
}
