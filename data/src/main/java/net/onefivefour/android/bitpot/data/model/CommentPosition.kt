package net.onefivefour.android.bitpot.data.model

/**
 * This class describes a position of a pull request comment.
 */
sealed class CommentPosition {

    /**
     * This is a comment on a single line within a File
     *
     * @param sourceLine the line number of the source file (the file being merged into the destination branch).
     * **Is -1 if the line does not exist in the source file.**
     *
     * @param destinationLine the line number of the destination file (the file that the source file is merged into)
     * **Is -1 if the line does not exist in the destination file.**
     *
     * @param filePath the path to the file to which the comment was attached to.
     */
    class Line(val sourceLine: Int, val destinationLine: Int, val filePath: String): CommentPosition()

    /**
     * This is a comment on a whole File
     *
     * @param filePath the path to the file to which the comment was attached to.
     */
    class File(val filePath: String): CommentPosition()

    /**
     * This is a comment for a whole Pull Request
     */
    object PullRequest : CommentPosition()
}

