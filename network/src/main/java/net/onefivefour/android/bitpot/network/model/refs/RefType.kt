package net.onefivefour.android.bitpot.network.model.refs

import com.google.gson.annotations.SerializedName

/**
 * This enum lists all possible [Ref]s of a repository.
 * Each RefType is initialized with its api string to
 * create the proper GET request.
 */
enum class RefType(val apiValue: String) {
    @SerializedName("branch")
    BRANCH("/branches"),
    @SerializedName("tag")
    TAG("/tags"),

    ALL("")
}