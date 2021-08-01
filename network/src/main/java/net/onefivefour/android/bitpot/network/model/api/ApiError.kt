package net.onefivefour.android.bitpot.network.model.api

import com.google.gson.annotations.SerializedName

/**
 * This data class represents an error object
 * from the Bitbucket Api
 */
data class ApiError(
    @SerializedName("type")
    val type: String,
    @SerializedName("error")
    val error: ErrorMessage
)

