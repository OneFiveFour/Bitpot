package net.onefivefour.android.bitpot.network.model.error

import com.google.gson.annotations.SerializedName

/**
 * Data class for some meta data when we receive a
 * [ApiError] from Bitbucket.
 */
data class ErrorData(
    @SerializedName("shas")
    val shas: List<String>
)