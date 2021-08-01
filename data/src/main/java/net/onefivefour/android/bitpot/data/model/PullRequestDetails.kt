package net.onefivefour.android.bitpot.data.model

/**
 * All details of a pull request that are needed in
 * PullRequestActivity. This is a complementary class to [PullRequest] with
 * [PullRequest] being only used in the list view of all pull requests.
 */
data class PullRequestDetails(
    val id: Int,
    val title: String,
    val descriptionMarkdown: String,
    var participants: List<Participant>,
    val state: PullRequestStatus
)