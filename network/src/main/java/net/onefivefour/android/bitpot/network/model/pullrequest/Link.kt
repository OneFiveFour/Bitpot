package net.onefivefour.android.bitpot.network.model.pullrequest

import com.google.gson.annotations.SerializedName

/**
 * This data class contains a href link
 */
data class Link(
    @SerializedName("href")
    val href: String
)