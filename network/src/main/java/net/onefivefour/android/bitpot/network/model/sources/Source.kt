package net.onefivefour.android.bitpot.network.model.sources

import com.google.gson.annotations.SerializedName

/**
 * A data class representing the network object
 * of a source (file or directory) of a repository
 */
data class Source(
    @SerializedName("commit")
    val commit: Commit,
    @SerializedName("path")
    val path: String,
    @SerializedName("size")
    val size: Long,
    @SerializedName("type")
    val type: String
)