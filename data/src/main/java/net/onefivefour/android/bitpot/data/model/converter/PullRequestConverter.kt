package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.extensions.toInstant
import net.onefivefour.android.bitpot.data.model.PullRequest
import net.onefivefour.android.bitpot.data.model.PullRequestState
import net.onefivefour.android.bitpot.network.model.pullrequests.PullRequest as NetworkPullRequest

/**
 * Converts a [NetworkPullRequest] into a app domain [PullRequest].
 */
class PullRequestConverter : NetworkDataConverter<NetworkPullRequest, PullRequest> {

    override fun toAppModel(item: NetworkPullRequest): PullRequest {
        return PullRequest(
            item.links.self.href,
            item.id,
            item.destination.repository.uuid,
            item.source.branch.name,
            item.destination.branch.name,
            item.updatedOn.toInstant(),
            toPullRequestState(item.state),
            item.author.links.avatar.href
        )
    }

    private fun toPullRequestState(state: String): PullRequestState {
        return when (state) {
            "OPEN" -> PullRequestState.OPEN
            "MERGED" -> PullRequestState.MERGED
            "DECLINED" -> PullRequestState.DECLINED
            "SUPERSEDED" -> PullRequestState.SUPERSEDED
            else -> PullRequestState.UNKNOWN
        }
    }
}
