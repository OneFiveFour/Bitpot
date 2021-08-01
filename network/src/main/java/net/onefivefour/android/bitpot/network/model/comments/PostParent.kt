package net.onefivefour.android.bitpot.network.model.comments

import com.google.gson.annotations.SerializedName

/**
 * A reference to an optional parentId of a comment.
 * If a comment has such a Parent it means that it is a reply to this Parent.
 */
data class PostParent(
    @SerializedName("id")
    val id: Int
)