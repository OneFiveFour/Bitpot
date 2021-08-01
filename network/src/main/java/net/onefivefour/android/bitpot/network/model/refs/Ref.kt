package net.onefivefour.android.bitpot.network.model.refs

import com.google.gson.annotations.SerializedName

/**
 * A data class representing the network object
 * of a repository reference (i.e. branch or tag)
 */
data class Ref(
    @SerializedName("type")
    val type: RefType,
    @SerializedName("name")
    val name: String,
    @SerializedName("target")
    val target: Target
)