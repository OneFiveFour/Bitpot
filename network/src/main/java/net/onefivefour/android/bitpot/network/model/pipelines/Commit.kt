package net.onefivefour.android.bitpot.network.model.pipelines

import com.google.gson.annotations.SerializedName

/**
 * A commit of a [Pipeline]
 */
data class Commit(
    @SerializedName("message")
    val message: String
)