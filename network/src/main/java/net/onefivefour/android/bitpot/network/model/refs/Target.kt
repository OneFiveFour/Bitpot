package net.onefivefour.android.bitpot.network.model.refs
import com.google.gson.annotations.SerializedName

/**
 * The target at which a [Ref] is aligned to.
 */
data class Target(
    @SerializedName("hash")
    val hash: String,
    @SerializedName("repository")
    val repository: Repository
)