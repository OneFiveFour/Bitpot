package net.onefivefour.android.bitpot.data.model

/**
 * An enum representing every possible state of a [PullRequest]
 */
enum class PullRequestState {
    OPEN,
    MERGED,
    DECLINED,
    SUPERSEDED,
    UNKNOWN
}
