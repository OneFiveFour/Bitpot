package net.onefivefour.android.bitpot.network.model.error

import com.google.gson.annotations.SerializedName

/**
 * Data class containing the error message and error data
 * once we receive an [ApiError] from Bitbucket.
 */
data class ErrorDescription(
    @SerializedName("data")
    val errorData: ErrorData,
    @SerializedName("message")
    val message: String
)