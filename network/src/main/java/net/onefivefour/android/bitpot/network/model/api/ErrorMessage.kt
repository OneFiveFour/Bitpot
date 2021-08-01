package net.onefivefour.android.bitpot.network.model.api

import com.google.gson.annotations.SerializedName

/**
 * This data class is part of an error object
 * from the Bitbucket Api
 */
data class ErrorMessage(
    @SerializedName("message")
    val message: String
)