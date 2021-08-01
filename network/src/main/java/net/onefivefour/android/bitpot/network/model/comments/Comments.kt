package net.onefivefour.android.bitpot.network.model.comments

import com.google.gson.annotations.SerializedName
import net.onefivefour.android.bitpot.network.model.api.PagedResponse

/**
 * This data class represents a pageable list of
 * pull request comment in Bitbucket.
 */
data class Comments(
    @SerializedName("page")
    override val page: Int,
    @SerializedName("pagelen")
    val pageLength: Int,
    @SerializedName("values")
    override val values: List<Comment>,
    @SerializedName("size")
    override val totalItems: Int,
    @SerializedName("next")
    override val nextPageUrl: String?
): PagedResponse<Comment>