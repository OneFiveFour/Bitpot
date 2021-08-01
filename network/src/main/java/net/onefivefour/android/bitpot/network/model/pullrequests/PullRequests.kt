package net.onefivefour.android.bitpot.network.model.pullrequests
import com.google.gson.annotations.SerializedName
import net.onefivefour.android.bitpot.network.model.api.PagedResponse

/**
 * The paged response from the Bitbucket API
 * that describes a list of [PullRequest]s
 */
data class PullRequests(
    @SerializedName("page")
    override val page: Int,
    @SerializedName("pagelen")
    val pageLength: Int,
    @SerializedName("values")
    override val values: List<PullRequest>,
    @SerializedName("size")
    override val totalItems: Int,
    @SerializedName("next")
    override val nextPageUrl: String?
): PagedResponse<PullRequest>

