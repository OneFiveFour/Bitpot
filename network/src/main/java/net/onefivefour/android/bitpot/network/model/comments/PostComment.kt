package net.onefivefour.android.bitpot.network.model.comments
import com.google.gson.annotations.SerializedName

/**
 * A comment in a pull request.
 * Either on the PR itself, on a file or on a line of code.
 */
data class PostComment(
    @SerializedName("content")
    val content: PostContent,

    /**
     * Null if the comment is not part of a file.
     * For example, if the comment was made for the whole PR.
     */
    @SerializedName("inline")
    val inlinePosition: Inline?,

    /**
     * Null if this comment is not a reply to another comment.
     */
    @SerializedName("parent")
    val parent: PostParent?
)
