package net.onefivefour.android.bitpot.data.model

/**
 * This data class is used to create a new comment.
 * It contains only fields needed to send it to the API POST Endpoint.
 *
 * @param content The comment that the user wants to send
 *
 * @param parentId the id of a comment that the user replied to. If it is not a reply, but a new comment, this is null
 *
 * @param position: the position of the new comment.
 */
data class PostComment(
    val content: String,
    val parentId: Int?,
    val position: CommentPosition
)