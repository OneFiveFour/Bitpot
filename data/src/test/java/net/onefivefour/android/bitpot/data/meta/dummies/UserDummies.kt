package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.approve.User as ApprovalUser
import net.onefivefour.android.bitpot.network.model.comments.User as CommentUser
import net.onefivefour.android.bitpot.network.model.pullrequest.User as PullRequestUser

object UserDummies {

    fun getCommentUser(): CommentUser {

        val displayName = StringDummies.getDisplayName()
        val links = LinksDummies.getCommentUserLinks()
        val accountId = "accountId"

        return CommentUser(
            displayName,
            links,
            accountId
        )
    }

    fun getPullRequestUser() : PullRequestUser {
        val accountId = "PullRequestUser.accountId"
        val links = LinksDummies.getPullRequestLinks()

        return PullRequestUser(accountId, links)
    }

    fun getApprovalUser(): ApprovalUser {

        val links = LinksDummies.getApprovalUserLinks()
        val accountId = "ApprovalUser.accountId"
        return ApprovalUser(accountId, links)
    }

}
