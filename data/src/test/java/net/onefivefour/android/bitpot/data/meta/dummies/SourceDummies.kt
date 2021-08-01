package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.sources.Source
import net.onefivefour.android.bitpot.network.model.pullrequests.Source as PullRequestsSource

object SourceDummies {

    fun getPullRequestsSource(): PullRequestsSource {
        val branch = BranchDummies.getPullRequestsBranch()
        return PullRequestsSource(branch)
    }

    fun getSource() : Source {

        val path = StringDummies.getPath()
        val type = "commit_directory"
        val size = 123L
        val commit = CommitDummies.getSourceCommit()

        return Source(
            commit,
            path,
            size,
            type
        )
    }
}
