package net.onefivefour.android.bitpot.network.model.pullrequest

import com.google.gson.annotations.SerializedName

/**
 * A rendered version of the description of a pull request
 */
data class Rendered(
    @SerializedName("description")
    val description: Description
)