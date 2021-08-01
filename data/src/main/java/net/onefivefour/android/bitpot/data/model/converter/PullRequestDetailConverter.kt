package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.model.PullRequestDetails
import net.onefivefour.android.bitpot.data.model.PullRequestStatus
import net.onefivefour.android.bitpot.network.model.pullrequest.PullRequest

/**
 * Converts a [PullRequest] into a app domain [PullRequestDetails].
 */
class PullRequestDetailConverter : NetworkDataConverter<PullRequest, PullRequestDetails> {

    override fun toAppModel(item: PullRequest): PullRequestDetails {

        val id = item.id
        val title = item.title
        val descriptionMarkdown = item.rendered.description.raw
        val participants = PullRequestParticipantsConverter().toAppModel(item.participants)
        val state = toPullRequestStatus(item.state)

        return PullRequestDetails(
            id,
            title,
            descriptionMarkdown,
            participants,
            state
        )
    }

    private fun toPullRequestStatus(state: String): PullRequestStatus {
        return when (state) {
            "MERGED" -> PullRequestStatus.MERGED
            "OPEN" -> PullRequestStatus.OPEN
            else -> PullRequestStatus.UNKNOWN
        }
    }
}
