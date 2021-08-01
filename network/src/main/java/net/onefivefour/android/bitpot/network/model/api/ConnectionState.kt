package net.onefivefour.android.bitpot.network.model.api

/**
 * All states of connections that can be emitted as ConnectionLiveData.
 * This one is used within the network module
 */
enum class ConnectionState {
    IS_CONNECTED,
    IS_DISCONNECTED,
    UNKNOWN
}