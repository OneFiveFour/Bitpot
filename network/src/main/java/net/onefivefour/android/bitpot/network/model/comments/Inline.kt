package net.onefivefour.android.bitpot.network.model.comments

import com.google.gson.annotations.SerializedName

/**
 * If the [Comment] was created on a line of code,
 * this class gives all information needed to find out which line that was.
 */
data class Inline(
    @SerializedName("from")
    val from: Int?,
    @SerializedName("path")
    val path: String,
    @SerializedName("to")
    val to: Int?
)