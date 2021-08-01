package net.onefivefour.android.bitpot.network.model.workspace

import com.google.gson.annotations.SerializedName
import net.onefivefour.android.bitpot.network.model.api.PagedResponse

/**
 * This data class represents a pageable list of
 * workspaces in Bitbucket.
 */
data class Workspaces(
    @SerializedName("page")
    override val page: Int,
    @SerializedName("pagelen")
    val pageLength: Int,
    @SerializedName("values")
    override val values: List<Workspace>,
    @SerializedName("size")
    override val totalItems: Int,
    @SerializedName("next")
    override val nextPageUrl: String?
): PagedResponse<Workspace>