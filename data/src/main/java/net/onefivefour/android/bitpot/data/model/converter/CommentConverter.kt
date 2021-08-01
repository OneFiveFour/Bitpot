package net.onefivefour.android.bitpot.data.model.converter

import net.onefivefour.android.bitpot.data.BitpotData
import net.onefivefour.android.bitpot.data.common.DataNetworkConverter
import net.onefivefour.android.bitpot.data.common.NetworkDataConverter
import net.onefivefour.android.bitpot.data.extensions.toInstant
import net.onefivefour.android.bitpot.data.model.Comment
import net.onefivefour.android.bitpot.data.model.CommentAuthor
import net.onefivefour.android.bitpot.data.model.CommentPosition
import net.onefivefour.android.bitpot.network.model.comments.Inline
import net.onefivefour.android.bitpot.network.model.comments.PostContent
import net.onefivefour.android.bitpot.network.model.comments.PostParent
import net.onefivefour.android.bitpot.data.model.PostComment as AppPostComment
import net.onefivefour.android.bitpot.network.model.comments.Comment as NetworkComment
import net.onefivefour.android.bitpot.network.model.comments.PostComment as NetworkPostComment
import net.onefivefour.android.bitpot.network.model.comments.User as NetworkCommentUser

/**
 * Converts a [NetworkComment] into a app domain [Comment].
 */
class CommentConverter : NetworkDataConverter<NetworkComment, Comment>, DataNetworkConverter<AppPostComment, NetworkPostComment> {


    override fun toAppModel(item: NetworkComment): Comment {

        val id = item.id
        val parentId = item.parent?.id
        val pullRequestId = item.pullRequest.id
        val content = item.content.raw
        val author = toCommentAuthor(item.user)
        val position = toCommentPosition(item.inlinePosition)
        val createdOn = item.createdOn.toInstant()
        val updatedOn = item.updatedOn.toInstant()
        val allowDelete = author.accountId == BitpotData.getAccountId()

        return Comment(
            id,
            parentId,
            pullRequestId,
            content,
            author,
            position,
            createdOn,
            updatedOn,
            allowDelete
        )
    }

    private fun toCommentPosition(inline: Inline?): CommentPosition {

        if (inline == null) return CommentPosition.PullRequest

        val sourceLine = inline.from ?: -1
        val destinationLine = inline.to ?: -1
        val filePath = inline.path

        val isFileComment = sourceLine == -1 && destinationLine == -1

        return when {
            isFileComment -> CommentPosition.File(filePath)
            else -> CommentPosition.Line(sourceLine, destinationLine, filePath)
        }
    }

    private fun toCommentAuthor(user: NetworkCommentUser): CommentAuthor {

        val accountId = user.accountId
        val displayName = user.displayName
        val avatarUrl = user.links.avatar.href

        return CommentAuthor(
            accountId,
            avatarUrl,
            displayName
        )
    }


    override fun toNetworkModel(from: AppPostComment): NetworkPostComment {
        val content = PostContent(from.content)
        val inlinePosition = toInlinePosition(from.position)
        val parent = toPostParent(from.parentId)

        return NetworkPostComment(
            content,
            inlinePosition,
            parent
        )
    }

    private fun toPostParent(parentId: Int?): PostParent? {
        return when (parentId) {
            null -> null
            else -> PostParent(parentId)
        }
    }

    private fun toInlinePosition(position: CommentPosition): Inline? {
        return when (position) {
            is CommentPosition.PullRequest -> null
            is CommentPosition.Line -> {
                val from = if (position.sourceLine == -1) null else position.sourceLine
                val to = if (position.destinationLine == -1) null else position.destinationLine
                Inline(from, position.filePath, to)
            }
            is CommentPosition.File -> Inline(null, position.filePath, null)
        }
    }

}
