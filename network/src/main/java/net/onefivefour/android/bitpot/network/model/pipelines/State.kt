package net.onefivefour.android.bitpot.network.model.pipelines

import com.google.gson.annotations.SerializedName

/**
 * Data class for the current state of a Pipeline
 */
data class State(
    @SerializedName("name")
    val name: String,
    @SerializedName("stage")
    val stage: Stage?,
    @SerializedName("result")
    val result: Result?,
    @SerializedName("type")
    val type: String
)