package net.onefivefour.android.bitpot.network.model.repositories
import com.google.gson.annotations.SerializedName

/**
 * A branch of a [Repository].
 */
data class Branch(
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String
)