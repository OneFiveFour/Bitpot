package net.onefivefour.android.bitpot.data.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.onefivefour.android.bitpot.network.model.api.NetworkState
import net.onefivefour.android.bitpot.network.model.api.NetworkStatus

/**
 * Enum of all possible Status of loading paging data
 */
enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

/**
 * This class basically represents the same model like NetworkStatus.
 * But since the UI layer has no access to the NetworkStatus class, this class
 * was created to tell the UI about the "Data Status".
 */
@Suppress("DataClassPrivateConstructor")
data class DataState private constructor(val status: Status, val msg: String? = null) {
    companion object {
        val LOADED = DataState(Status.SUCCESS)
        val LOADING = DataState(Status.RUNNING)
        private fun error(msg: String?) = DataState(Status.FAILED, msg)

        /**
         * Converts the given NetworkState to LiveData of DataState
         */
        fun from(networkState: NetworkState?): LiveData<DataState> {
            val result = MutableLiveData<DataState>()
            result.value = when (networkState?.networkStatus) {
                NetworkStatus.RUNNING -> LOADING
                NetworkStatus.SUCCESS -> LOADED
                NetworkStatus.FAILED -> error(networkState.msg)
                else -> LOADING
            }

            return result
        }
    }
}