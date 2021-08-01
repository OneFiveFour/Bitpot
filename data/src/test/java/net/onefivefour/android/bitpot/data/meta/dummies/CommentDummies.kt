package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.data.model.PostComment
import net.onefivefour.android.bitpot.network.model.comments.Comment as NetworkComment


object CommentDummies {


    fun getNetworkComment(): NetworkComment {

        val content = ContentDummies.getContent()
        val createdOn = StringDummies.getDateTimeString()
        val id = 1
        val inline = InlineDummies.getInline()
        val parent = ParentDummies.getCommentParent()
        val pullRequest = PullRequestDummies.getCommentPullRequest()
        val updatedOn = StringDummies.getDateTimeString()
        val user = UserDummies.getCommentUser()

        return NetworkComment(
            content,
            createdOn,
            id,
            inline,
            parent,
            pullRequest,
            updatedOn,
            user
        )
    }

    fun getPostComment(): PostComment {
        val content = StringDummies.getRawString()
        val parentId = 1
        val position = CommentPositionDummies.getPullRequestCommentPosition()

        return PostComment(
            content,
            parentId,
            position
        )
    }

}