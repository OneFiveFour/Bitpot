package net.onefivefour.android.bitpot.network.model.pullrequests
import com.google.gson.annotations.SerializedName

/**
 * This data class represents a git branch
 * of a git repository
 */
data class Branch(
    @SerializedName("name")
    val name: String
)