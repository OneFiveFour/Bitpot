package net.onefivefour.android.bitpot.network.model.sources

import com.google.gson.annotations.SerializedName

/**
 * A commit of a [net.onefivefour.android.bitpot.network.model.repositories.Repository]
 */
data class Commit(
    @SerializedName("hash")
    val hash: String
)