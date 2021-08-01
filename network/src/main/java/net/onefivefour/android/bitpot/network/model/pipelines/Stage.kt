package net.onefivefour.android.bitpot.network.model.pipelines

import com.google.gson.annotations.SerializedName

/**
 * This class represents the stage of a [Pipeline]
 */
data class Stage(
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String
)