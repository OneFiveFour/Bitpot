package net.onefivefour.android.bitpot.network.model.webhooks

import com.google.gson.annotations.SerializedName
import net.onefivefour.android.bitpot.network.model.api.PagedResponse

/**
 * This data class represents a pageable list of WebHooks.
 */
data class WebHooks(
    @SerializedName("page")
    override val page: Int,
    @SerializedName("pagelen")
    val pageLength: Int,
    @SerializedName("values")
    override val values: List<WebHook>,
    @SerializedName("size")
    override val totalItems: Int,
    @SerializedName("next")
    override val nextPageUrl: String?
): PagedResponse<WebHook>