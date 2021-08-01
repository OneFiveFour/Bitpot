package net.onefivefour.android.bitpot.network.model.api

/**
 * Data class that is used to represent the loading state of paging data
 */
@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(val networkStatus: NetworkStatus, val msg: String? = null) {
    companion object {
        val LOADED = NetworkState(NetworkStatus.SUCCESS)
        val LOADING = NetworkState(NetworkStatus.RUNNING)
        fun error(msg: String?) =
            NetworkState(NetworkStatus.FAILED, msg)
    }
}