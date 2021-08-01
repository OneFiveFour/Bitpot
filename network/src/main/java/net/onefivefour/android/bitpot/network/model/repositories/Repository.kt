package net.onefivefour.android.bitpot.network.model.repositories

import com.google.gson.annotations.SerializedName
import net.onefivefour.android.bitpot.network.model.pipelines.Pipeline

/**
 * This data class represents a git repository
 * from Bitbucket.
 */
data class Repository(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("is_private")
    val isPrivate: Boolean,
    @SerializedName("links")
    val links: Links,
    @SerializedName("name")
    val name: String,
    @SerializedName("workspace")
    val workspace: Workspace,
    @SerializedName("updated_on")
    val updatedOn: String,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("mainbranch")
    val mainBranch: Branch?
    
) {
    var lastPipeline: Pipeline? = null
}