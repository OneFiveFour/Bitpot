package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.approve.Participant as ApprovalParticipant
import net.onefivefour.android.bitpot.network.model.pullrequest.Participant as PullRequestParticipant

object ParticipantDummies {

    fun getPullRequestParticipant() : PullRequestParticipant {
        val approved = true
        val user = UserDummies.getPullRequestUser()

        return PullRequestParticipant(approved, user)
    }

    fun getPullRequestParticipants(): List<PullRequestParticipant> {
        return listOf(
            getPullRequestParticipant(),
            getPullRequestParticipant(),
            getPullRequestParticipant()
        )
    }

    fun getApprovalParticipant() : ApprovalParticipant {
        val approved = true
        val user = UserDummies.getApprovalUser()

        return ApprovalParticipant(approved, user)
    }

}