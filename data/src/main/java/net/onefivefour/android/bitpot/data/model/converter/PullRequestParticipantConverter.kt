package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.model.Participant as AppParticipant
import net.onefivefour.android.bitpot.network.model.pullrequest.Participant as NetworkParticipant

/**
 * Converts a [NetworkParticipant] object into a [AppParticipant].
 */
class PullRequestParticipantConverter : NetworkDataConverter<NetworkParticipant, AppParticipant> {
    override fun toAppModel(item: NetworkParticipant): AppParticipant {
        val accountId = item.user.accountId
        val avatarUrl = item.user.links.avatar.href
        val hasApproved = item.approved

        return AppParticipant(accountId, avatarUrl, hasApproved)
    }
}
