package net.onefivefour.android.bitpot.network.model.pipelines

import com.google.gson.annotations.SerializedName

/**
 * Data class describing the current result of a Pipeline
 */
data class Result(
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String
)