package net.onefivefour.android.bitpot.network.model.pullrequest

import com.google.gson.annotations.SerializedName

/**
 * The description of a pull request.
 * Either as HTML or as raw/markdown string.
 */
data class Description(
    @SerializedName("raw")
    val raw: String
)