package net.onefivefour.android.bitpot.network.model.pipelines

import com.google.gson.annotations.SerializedName

/**
 * Data class describing a Pipeline
 */
data class Pipeline(
    @SerializedName("build_number")
    val buildNumber: Int,
    @SerializedName("created_on")
    val createdOn: String,
    @SerializedName("repository")
    val repository: Repository,
    @SerializedName("state")
    val state: State,
    @SerializedName("target")
    val target: Target,
    @SerializedName("uuid")
    val uuid: String
)