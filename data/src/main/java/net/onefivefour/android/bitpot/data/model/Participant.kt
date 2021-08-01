package net.onefivefour.android.bitpot.data.model

/**
 * A person that is part of the review process of a [PullRequestDetails]
 */
data class Participant(
    val accountId: String,
    val avatarUrl: String,
    var hasApproved: Boolean
)