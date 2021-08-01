package net.onefivefour.android.bitpot.network.model.webhooks

import com.google.gson.annotations.SerializedName

/**
 * The subject that is part of a WebHook.
 */
data class Subject(
    @SerializedName("uuid")
    val uuid: String
)