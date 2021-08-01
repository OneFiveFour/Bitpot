package net.onefivefour.android.bitpot.data.model

/**
 * Data class for a line of code in a [FileDiff]
 */
data class DiffLine(
    val type: DiffLineType,
    val sourceLineNumber: Int,
    val destinationLineNumber: Int,
    val content: String,

    // mutable to set the list of comments as soon as we get them from the backend.
    var comments: List<Comment> = emptyList()
)
