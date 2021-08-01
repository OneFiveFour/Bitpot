package net.onefivefour.android.bitpot.network.model.refs

import com.google.gson.annotations.SerializedName
import net.onefivefour.android.bitpot.network.model.api.PagedResponse

/**
 * This class represents a list of branches and tags of
 * the source code of a repository coming from the Bitbucket API.
 */
data class Refs(
    @SerializedName("page")
    override val page: Int,
    @SerializedName("pagelen")
    val pageLength: Int,
    @SerializedName("values")
    override val values: List<Ref>,
    @SerializedName("size")
    override val totalItems: Int,
    @SerializedName("next")
    override val nextPageUrl: String
): PagedResponse<Ref>