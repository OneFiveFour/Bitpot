package net.onefivefour.android.bitpot.network.model.pipelines

import com.google.gson.annotations.SerializedName

/**
 * Data class describing the target of a pipeline.
 */
data class Target(
    @SerializedName("commit")
    val commit: Commit,
    @SerializedName("destination")
    val destination: String,
    @SerializedName("ref_name")
    val refName: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("type")
    val type: String
)