package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.pullrequests.Branch as PullRequestsBranch
import net.onefivefour.android.bitpot.network.model.repositories.Branch as RepositoryBranch

object BranchDummies {
    
    fun getPullRequestsBranch(): PullRequestsBranch {
        val name = "net.onefivefour.android.bitpot.network.model.repositories.Branch.name"
        return PullRequestsBranch(name)
    }

    fun getMainBranch(): RepositoryBranch {
        
        val name = "master"
        val type = "branch"
        
        return RepositoryBranch(
            name,
            type
        )
    }

}
