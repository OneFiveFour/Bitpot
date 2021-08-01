package net.onefivefour.android.bitpot.data.model

/**
 * The author of a pull request comment.
 */
data class CommentAuthor(
    val accountId: String,
    val avatarUrl: String,
    val name: String
)