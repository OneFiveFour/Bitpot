package net.onefivefour.android.bitpot.network.model.downloads

import com.google.gson.annotations.SerializedName
import net.onefivefour.android.bitpot.network.model.api.PagedResponse

/**
 * This class represents the root element of the
 * paged downloads response from tbe Bitbucket API.
 */
data class Downloads(
    @SerializedName("page")
    override val page: Int,
    @SerializedName("pagelen")
    val pageLength: Int,
    @SerializedName("values")
    override val values: List<Download>,
    @SerializedName("size")
    override val totalItems: Int,
    @SerializedName("next")
    override val nextPageUrl: String

): PagedResponse<Download>