package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.network.model.approve.Link as ApprovalUserLink
import net.onefivefour.android.bitpot.network.model.approve.Links as ApprovalUserLinks
import net.onefivefour.android.bitpot.network.model.comments.Link as CommentUserLink
import net.onefivefour.android.bitpot.network.model.comments.Links as CommentUserLinks
import net.onefivefour.android.bitpot.network.model.downloads.Link as DownloadLink
import net.onefivefour.android.bitpot.network.model.downloads.Links as DownloadLinks
import net.onefivefour.android.bitpot.network.model.pullrequest.Link as PullRequestLink
import net.onefivefour.android.bitpot.network.model.pullrequest.Links as PullRequestLinks
import net.onefivefour.android.bitpot.network.model.pullrequests.Link as PullRequestsLink
import net.onefivefour.android.bitpot.network.model.pullrequests.Links as PullRequestsLinks
import net.onefivefour.android.bitpot.network.model.repositories.Link as RepositoryLink
import net.onefivefour.android.bitpot.network.model.repositories.Links as RepositoryLinks
import net.onefivefour.android.bitpot.network.model.workspace.Link as UserLink
import net.onefivefour.android.bitpot.network.model.workspace.Links as WorkspaceLinks

object LinksDummies {

    fun getPullRequestLinks(): PullRequestLinks {
        val avatarLink = PullRequestLink("http://avatarlink.com")
        return PullRequestLinks(avatarLink)
    }

    fun getPullRequestsLinks(): PullRequestsLinks {
        val avatarLink = PullRequestsLink("http://avatarlink.com")
        val selfLink = PullRequestsLink("http://self.com")
        return PullRequestsLinks(avatarLink, selfLink)
    }

    fun getRepositoryLinks(): RepositoryLinks {
        val avatarLink = RepositoryLink("http://avatarlink.com")
        return RepositoryLinks(avatarLink)
    }

    fun getWorkspaceLinks(): WorkspaceLinks {
        val avatarLink = UserLink("https://avatarlink.com")
        return WorkspaceLinks(avatarLink)
    }

    fun getCommentUserLinks(): CommentUserLinks {
        val avatarLink = CommentUserLink("http://avatarlink.com")
        return CommentUserLinks(avatarLink)
    }

    fun getApprovalUserLinks(): ApprovalUserLinks {
        val avatarLink = ApprovalUserLink("http://avatarlink.com")
        return ApprovalUserLinks(avatarLink)
    }

    fun getDownloadLinks(): DownloadLinks {
        val selfLink = DownloadLink("http://self.com")
        return DownloadLinks(selfLink)
    }
}
