package net.onefivefour.android.bitpot.network.model.repositories
import com.google.gson.annotations.SerializedName
import net.onefivefour.android.bitpot.network.model.api.PagedResponse

/**
 * This data class represents a pageable list of
 * git repositories in Bitbucket.
 */
data class Repositories(
    @SerializedName("page")
    override val page: Int,
    @SerializedName("pagelen")
    val pageLength: Int,
    @SerializedName("values")
    override val values: List<Repository>,
    @SerializedName("size")
    override val totalItems: Int,
    @SerializedName("next")
    override val nextPageUrl: String?
): PagedResponse<Repository>

