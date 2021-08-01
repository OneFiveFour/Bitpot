package net.onefivefour.android.bitpot.network.model.downloads
import com.google.gson.annotations.SerializedName

/**
 * This class represents a Download coming from the Bitbucket API
 */
data class Download(
    @SerializedName("created_on")
    val createdOn: String,
    @SerializedName("links")
    val links: Links,
    @SerializedName("name")
    val name: String,
    @SerializedName("size")
    val size: Long
)