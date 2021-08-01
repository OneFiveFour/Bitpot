package net.onefivefour.android.bitpot.network.model.error
import com.google.gson.annotations.SerializedName

/**
 * Data class representation of the Bitbucket error response.
 */
data class ApiError(
    @SerializedName("data")
    val errorData: ErrorData,
    @SerializedName("error")
    val errorDescription: ErrorDescription,
    @SerializedName("type")
    val type: String
)