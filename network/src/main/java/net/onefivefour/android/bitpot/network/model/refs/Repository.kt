package net.onefivefour.android.bitpot.network.model.refs

import com.google.gson.annotations.SerializedName

/**
 * This data class represents a git repository
 * from Bitbucket.
 */
data class Repository(
    @SerializedName("uuid")
    val uuid: String
)