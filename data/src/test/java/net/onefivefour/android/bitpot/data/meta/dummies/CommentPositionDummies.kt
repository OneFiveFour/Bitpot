package net.onefivefour.android.bitpot.data.meta.dummies

import net.onefivefour.android.bitpot.data.model.CommentPosition

object CommentPositionDummies {

    fun getPullRequestCommentPosition(): CommentPosition.PullRequest {
        return CommentPosition.PullRequest
    }

    fun getFileCommentPosition(): CommentPosition.File {
        val filePath = StringDummies.getPath()
        return CommentPosition.File(filePath)
    }

    fun getLineCommentPosition(): CommentPosition.Line {
        val filePath = StringDummies.getPath()
        val sourceLine = 1
        val destinationLine = -1
        return CommentPosition.Line(sourceLine, destinationLine, filePath)
    }
}
