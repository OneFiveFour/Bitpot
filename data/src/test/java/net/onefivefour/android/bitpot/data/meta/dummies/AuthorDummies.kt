package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.pullrequests.Author as PullRequestsAuthor

object AuthorDummies {

    fun getSimplePullRequestsAuthor(): PullRequestsAuthor {
        val links = LinksDummies.getPullRequestsLinks()
        return PullRequestsAuthor(links)
    }

}
