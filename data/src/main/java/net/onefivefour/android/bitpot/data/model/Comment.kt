package net.onefivefour.android.bitpot.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.Instant

/**
 * The data class representing a PullRequest comment.
 * This is also the description of the database table for downloads.
 */
@Entity(
    tableName = "comments",
    indices = [Index(value = ["id"], unique = true)]
)
data class Comment(
    @PrimaryKey
    val id: Int,

    val parentId: Int?,
    val pullRequestId: Int,
    val content: String,
    val author: CommentAuthor,
    val position: CommentPosition,
    val createdOn: Instant,
    val updatedOn: Instant,
    val allowDelete: Boolean

): DiffItem