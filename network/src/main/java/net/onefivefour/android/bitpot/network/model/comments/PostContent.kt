package net.onefivefour.android.bitpot.network.model.comments

import com.google.gson.annotations.SerializedName

/**
 * The content of a [Comment]
 * Basically the text that the author wrote as a comment.
 */
data class PostContent(
    @SerializedName("raw")
    val raw: String
)