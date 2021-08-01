package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.pullrequests.Destination as PullRequestsDestination

object DestinationDummies {
    fun getPullRequestsDestination(): PullRequestsDestination {

        val branch = BranchDummies.getPullRequestsBranch()
        val repository = RepositoryDummies.getPullRequestsRepository()

        return PullRequestsDestination(
            branch,
            repository
        )
    }

}
