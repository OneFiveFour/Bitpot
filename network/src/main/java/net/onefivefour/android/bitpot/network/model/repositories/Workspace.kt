package net.onefivefour.android.bitpot.network.model.repositories

import com.google.gson.annotations.SerializedName

/**
 * A workspace as part of a [Repository].
 */
data class Workspace(
    @SerializedName("uuid")
    val uuid: String
)