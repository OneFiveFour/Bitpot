package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.pullrequests.PullRequest
import net.onefivefour.android.bitpot.network.model.comments.PullRequest as CommentPullRequest
import net.onefivefour.android.bitpot.network.model.pullrequest.PullRequest as NetworkPullRequestDetails

object PullRequestDummies {

    fun getNetworkPullRequest(): PullRequest {

        val author = AuthorDummies.getSimplePullRequestsAuthor()
        val destination = DestinationDummies.getPullRequestsDestination()
        val id = 1
        val source = SourceDummies.getPullRequestsSource()
        val state = PullRequestStateDummies.getOpenState()
        val updatedOn = StringDummies.getDateTimeString()
        val links = LinksDummies.getPullRequestsLinks()
        
        return PullRequest(
            author,
            destination,
            id,
            links,
            source,
            state.name,
            updatedOn
        )
    }

    fun getNetworkPullRequestDetails(): NetworkPullRequestDetails {

        val id = 1
        val participants = ParticipantDummies.getPullRequestParticipants()
        val rendered = RenderedDummies.getRendered()
        val state = "MERGED"
        val title = StringDummies.getSimpleString()

        return NetworkPullRequestDetails(
            id,
            participants,
            rendered,
            state,
            title
        )
    }

    fun getCommentPullRequest(): CommentPullRequest {
        val id = 1
        return CommentPullRequest(id)
    }

}
