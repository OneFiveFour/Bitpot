package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.model.Participant as AppParticipant
import net.onefivefour.android.bitpot.network.model.pullrequest.Participant as NetworkParticipant

/**
 * Converts a list of [NetworkParticipant]s into a list of [AppParticipant]s.
 */
class PullRequestParticipantsConverter : NetworkDataConverter<List<NetworkParticipant>, List<AppParticipant>> {
    override fun toAppModel(item: List<NetworkParticipant>): List<AppParticipant> {
        val participantConverter = PullRequestParticipantConverter()
        return item.map { participantConverter.toAppModel(it) }
    }
}
